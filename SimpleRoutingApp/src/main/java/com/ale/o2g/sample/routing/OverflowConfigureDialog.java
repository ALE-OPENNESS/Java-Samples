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

import com.ale.o2g.sample.routing.util.ConditionEnumConverter;
import com.ale.o2g.types.routing.Destination;
import com.ale.o2g.types.routing.Overflow.Condition;
import com.ale.o2g.types.routing.Overflow;

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
import javafx.util.StringConverter;

/**
 *
 */
public class OverflowConfigureDialog extends Dialog<Overflow> {

	private DialogPane dialogPane;
	private Overflow overflow;

	@FXML RadioButton rbNone;
	@FXML RadioButton rbVoicemail;
	@FXML ComboBox<Overflow.Condition> cbCondition;
	@FXML ButtonType buttonTypeApply;
	
	
	/**
	 * Create a controller
	 */
	public OverflowConfigureDialog(Overflow overflow) {
		this.overflow = overflow;

    	try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/overflow-config.fxml"));
    		fxmlLoader.setController(this);

            dialogPane = fxmlLoader.load();
            dialogPane.lookupButton(buttonTypeApply).addEventFilter(ActionEvent.ANY, this::onApply);

    		setTitle("Configure Overflow");
    		setDialogPane(dialogPane);
    		
    		setResultConverter(buttonType -> {
                if (!Objects.equals(ButtonBar.ButtonData.APPLY, buttonType.getButtonData())) {
                    return null;
                }

                if (rbNone.isSelected()) {
                	return new Overflow(Destination.NONE, null) {
                	};
                }
                else { 
                	
                	if (rbVoicemail.isSelected()) {
                		return new Overflow(Destination.VOICEMAIL, cbCondition.getValue()) {
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
		cbCondition.setItems(FXCollections.observableArrayList(Overflow.Condition.values()));
		cbCondition.setConverter(new StringConverter<Overflow.Condition>() {

		    @Override
		    public String toString(Overflow.Condition object) {
		        return ConditionEnumConverter.getDisplayName(object.name());
		    }

		    @Override
		    public Overflow.Condition fromString(String string) {
		    	String sEnum = ConditionEnumConverter.getEnumName(string);
		    	return Enum.valueOf(Overflow.Condition.class, sEnum);
		    }
		});

		
		if (overflow != null) {
			
			if (overflow.getDestination() == Destination.NONE) {
				rbNone.setSelected(true);
				cbCondition.setValue(Condition.NO_ANSWER);
			}
			else {
				cbCondition.setValue(overflow.getCondition());
				
				if (overflow.getDestination() == Destination.VOICEMAIL) {
					rbVoicemail.setSelected(true);
				}
			}
		}
		else {
			rbNone.setSelected(true);
			cbCondition.setValue(Condition.NO_ANSWER);
		}
	}
	
	
	@FXML
    private void onApply(ActionEvent event) {
		
	}
}
