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

import com.ale.o2g.types.routing.Destination;
import com.ale.o2g.types.routing.Forward;
import com.ale.o2g.types.routing.Forward.Condition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 */
public class ForwardControl extends AbstractRoutingControl {

	@FXML private Label labelNoForward;
	@FXML private Label forwardIcon;
	@FXML private Label labelNumber;
	@FXML private Label labelCondition;
		
	public void set(Forward forward) {
		
		if ((forward == null) || (forward.getDestination() == Destination.NONE)) {
			setVisible(labelNoForward, true);
			setVisible(forwardIcon, false);
			setVisible(labelNumber, false);
			setVisible(labelCondition, false);
			setVisible(buttonCancel, false);
            labelNumber.setText("");
		}
		else {

			setVisible(labelNoForward, false);
			setVisible(buttonCancel, true);
            if (forward.getDestination() == Destination.NUMBER) {
            	forwardIcon.setGraphic(new ImageView(this.getImgOnNumber()));
                labelNumber.setText(forward.getNumber());
                setVisible(labelNumber, true);
            }
            else {
            	forwardIcon.setGraphic(new ImageView(this.getImgOnVoiceMail()));
                labelNumber.setText("");
                setVisible(labelNumber, false);
            }
            setVisible(forwardIcon, true);

            labelCondition.setText(formatCondition(forward.getCondition()));
            setVisible(labelCondition, true);
        }

		setVisible(buttonConfig, true);
	}

	private static String formatCondition(Condition condition) {
	    if (condition == Condition.IMMEDIATE) {
	        return "Immediate";
	    }
	    else if (condition == Condition.BUSY) {
	        return "on busy";
	    }
	    else if (condition == Condition.NO_ANSWER) {
	        return "on no answer";
	    }
	    else {
	        return "on busy or no answer";
	    }
	}

	
	/**
	 * 
	 */
	public ForwardControl() {
		super("views/forward-control.fxml");
	}
}
