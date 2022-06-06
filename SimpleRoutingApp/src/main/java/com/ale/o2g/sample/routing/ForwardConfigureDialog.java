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

import java.util.Objects;

import com.ale.o2g.types.routing.Destination;
import com.ale.o2g.types.routing.Forward;
import com.ale.o2g.types.routing.Forward.Condition;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import com.ale.o2g.sample.routing.util.*;

/**
 *
 */
public class ForwardConfigureDialog extends Dialog<Forward> {

	private DialogPane dialogPane;
	private Forward forward;

	@FXML RadioButton rbNone;
	@FXML RadioButton rbVoicemail;
	@FXML RadioButton rbNumber;
	@FXML TextField tfNumber;
	@FXML ComboBox<Forward.Condition> cbCondition;
	@FXML ButtonType buttonTypeApply;
	
	
	/**
	 * Create a controller
	 */
	public ForwardConfigureDialog(Forward forward) {
		this.forward = forward;

    	try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/forward-config.fxml"));
    		fxmlLoader.setController(this);

            dialogPane = fxmlLoader.load();
            dialogPane.lookupButton(buttonTypeApply).addEventFilter(ActionEvent.ANY, this::onApply);

    		setTitle("Configure forward");
    		setDialogPane(dialogPane);
    		
    		setResultConverter(buttonType -> {
                if (!Objects.equals(ButtonBar.ButtonData.APPLY, buttonType.getButtonData())) {
                    return null;
                }

                if (rbNone.isSelected()) {
                	return new Forward(Destination.NONE, null, null) {
                	};
                }
                else { 

                	Condition condition = cbCondition.getValue();
                	
                	if (rbVoicemail.isSelected()) {
                		return new Forward(Destination.VOICEMAIL, condition, null) {
                		};
                	}
                	else if (rbNumber.isSelected()) {
                		return new Forward(Destination.NUMBER, condition, tfNumber.getText()) {
                		};
                	}
                	else {
                		return null;
                	}
                }
            });
    	}
    	catch (Exception e) {
        	e.printStackTrace();
    	}
	}

	@FXML
    private void initialize() {

		// Init items
		cbCondition.setItems(FXCollections.observableArrayList(Forward.Condition.values()));
		cbCondition.setConverter(new StringConverter<Forward.Condition>() {

		    @Override
		    public String toString(Forward.Condition object) {
		        return ConditionEnumConverter.getDisplayName(object.name());
		    }

		    @Override
		    public Forward.Condition fromString(String string) {
		    	String sEnum = ConditionEnumConverter.getEnumName(string);
		    	return Enum.valueOf(Forward.Condition.class, sEnum);
		    }
		});
		
		
		if (forward != null) {
			
			if (forward.getDestination() == Destination.NONE) {
				rbNone.setSelected(true);
				cbCondition.setValue(Condition.IMMEDIATE);
			}
			else {
				cbCondition.setValue(forward.getCondition());
				
				if (forward.getDestination() == Destination.VOICEMAIL) {
					rbVoicemail.setSelected(true);
				}
				else if (forward.getDestination() == Destination.NUMBER) {
					rbNumber.setSelected(true);
					tfNumber.setText(forward.getNumber());
				}
			}
		}
		else {
			rbNone.setSelected(true);
			cbCondition.setValue(Condition.IMMEDIATE);
		}
	}
	
	
	@FXML
    private void onApply(ActionEvent event) {
		
	}
}
