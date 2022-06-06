/*
* Copyright 2022 ALE International
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this 
* software and associated documentation files (the "Software"), to deal in the Software 
* without restriction, including without limitation the rights to use, copy, modify, merge, 
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons 
* to whom the Software is furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ale.o2g.sample.routing;



import com.ale.o2g.O2GException;
import com.ale.o2g.RoutingService;
import com.ale.o2g.Session;
import com.ale.o2g.Subscription;
import com.ale.o2g.TelephonyService;
import com.ale.o2g.UsersService;
import com.ale.o2g.events.routing.OnRoutingStateChangedEvent;
import com.ale.o2g.events.routing.RoutingEventListener;
import com.ale.o2g.types.routing.Destination;
import com.ale.o2g.types.routing.RoutingState;
import com.ale.o2g.types.telephony.device.DeviceState;
import com.ale.o2g.types.telephony.device.OperationalState;
import com.ale.o2g.types.users.Device;
import com.ale.o2g.types.users.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


/**
 * The controller class for the Routing form
 *
 */
public class RoutingController {

	private Session session;
	private RoutingState routingState;
	
	@FXML private Label labelName;
	@FXML private Label labelCompanyPhone;
	@FXML private Label labelStatus;
	@FXML private Label labelVoiceMail;
	@FXML private CheckBox cbDnd;
	@FXML private ForwardControl forward;
	@FXML private OverflowControl overflow;
	 
	
	/**
	 * Construct a new Routing Controller
	 * @param session the opened O2G session
	 */
	public RoutingController(Session session) {
		this.session = session;
	}

	
	/**
	 * Close the session
	 * @throws O2GException 
	 */
	public void shutdown() {
		
		if (this.session != null) {
			try {
				this.session.close();
			} 
			catch (O2GException e) {
				e.printStackTrace();
			}
		}
		
		Platform.exit();
	}
	
	
	@FXML
    private void initialize() {
		
		Subscription subscription = Subscription.newBuilder().addRoutingEventListener(new RoutingEventListener() {

			@Override
			public void onRoutingStateChanged(final OnRoutingStateChangedEvent event) {
				
				Platform.runLater(() -> {
					setRoutingState(event.getRoutingState());
				});
			}
			
		}).build();
		
		try {
			session.listenEvents(subscription);
		} 
		catch (O2GException e) {
			e.printStackTrace();
		}

		load();
	}		

	/**
	 * Load the data from O2G.
	 * @implNote : should be done asynchronously for better prformance
	 */
    private void load() {
		
	    // Load the user information
		UsersService usersService = session.getUsersService();
		
		User user = usersService.getByLoginName(session.getLoginName());

		labelName.setText(String.format("%1s %2s", user.getFirstName(), user.getLastName()));
		labelCompanyPhone.setText(user.getCompanyPhone());
		
		if (user.getVoicemail() != null) {
			labelVoiceMail.setText(String.format("Voice mail : %1s", user.getVoicemail().getNumber()));
		}
		else {
			labelVoiceMail.setText("No voice mail");	
		}
		
		Device mainDevice = user.getDevices().iterator().next();
		
		TelephonyService telephonyService = session.getTelephonyService();
	    DeviceState deviceState = telephonyService.getDeviceState(mainDevice.getId());
	    setDeviceState(deviceState);

	    RoutingService routingService = session.getRoutingService();
	    setRoutingState(routingService.getRoutingState());
	}	

	/**
	 * Set the status of the device on the GUI.
	 * @param deviceState the device state
	 */
    private void setDeviceState(DeviceState deviceState) {
        OperationalState operationalState = deviceState.getState();
        if (operationalState == OperationalState.IN_SERVICE) {
            labelStatus.setText("In Service");
            labelStatus.setTextFill(Color.BLUE);
        }
        else {
            labelStatus.setText("Out of service");
            labelStatus.setTextFill(Color.RED);
        }
    }

    /**
     * Update the GUI according to the routing state.
     * @param routingState the routing state
     */
    private void setRoutingState(RoutingState routingState) {
        try {
            this.routingState = routingState;
            forward.set(routingState.getForward());
            overflow.set(routingState.getOverflow());
            
            cbDnd.setSelected(routingState.getDndState().isActivated());
            cbDnd.setVisible(true);
        }
        catch (Exception e) {
        	e.printStackTrace();
        };
    }
    
    @FXML
    private void onForwardCancel(ActionEvent event) {
		RoutingService routingService = session.getRoutingService();
		routingService.cancelForward();
    }
    
    
    @FXML
    private void onForwardConfigure(ActionEvent event) {

    	ForwardConfigureDialog dialog = new ForwardConfigureDialog(routingState.getForward());
    	dialog.showAndWait().ifPresent(forward -> {
    		
    		if (forward != null) {
        		RoutingService routingService = session.getRoutingService();
    			
    			if (forward.getDestination() == Destination.NONE) {
    				routingService.cancelForward();
    			}
    			else if (forward.getDestination() == Destination.VOICEMAIL) {
    				routingService.forwardOnVoiceMail(forward.getCondition());
    			}
    			else if (forward.getDestination() == Destination.NUMBER) {
    				routingService.forwardOnNumber(forward.getNumber(), forward.getCondition());
    			}
    		}
    	});   
    }
    	
    @FXML
    private void onOverflowCancel(ActionEvent event) {
		RoutingService routingService = session.getRoutingService();
		routingService.cancelOverflow();
    }
    
    @FXML
    private void onOverflowConfigure(ActionEvent event) {

    	OverflowConfigureDialog dialog = new OverflowConfigureDialog(routingState.getOverflow());
    	dialog.showAndWait().ifPresent(overflow -> {
    		
    		if (overflow != null) {
        		RoutingService routingService = session.getRoutingService();
    			
    			if (overflow.getDestination() == Destination.NONE) {
    				routingService.cancelOverflow();
    			}
    			else if (overflow.getDestination() == Destination.VOICEMAIL) {
    				routingService.overflowOnVoiceMail(overflow.getCondition());
    			}
    		}
    	});   
    }
    	
    @FXML
    private void onDndAction() {
    	
	    RoutingService routingService = session.getRoutingService();
   	
        if (cbDnd.isSelected()) {
            if (!routingService.activateDnd()) {
                cbDnd.setSelected(false);
            }
        }
        else {
            if (!routingService.cancelDnd()) {
                cbDnd.setSelected(true);
            }
        }
    }
}