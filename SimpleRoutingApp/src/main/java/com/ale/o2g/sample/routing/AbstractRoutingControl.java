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

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

/**
 *
 */
public abstract class AbstractRoutingControl extends HBox {

	private static Image imgOnNumber;
	private static Image imgOnVoiceMail;	
	
	@FXML protected Button buttonCancel;
	@FXML protected Button buttonConfig;

	protected Image getImgOnNumber() {
		if (imgOnNumber == null) {
			imgOnNumber = new Image(getClass().getResourceAsStream("images/forward_number.png"));
		}
		return imgOnNumber;
	}

	protected Image getImgOnVoiceMail() {
		if (imgOnVoiceMail == null) {
			imgOnVoiceMail = new Image(getClass().getResourceAsStream("images/forward_voicemail.png"));
		}
		return imgOnVoiceMail;
	}
	
	@FXML
	private void onConfigure(ActionEvent event) {
		onConfigureProperty().get().handle(event);	
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		onCancelProperty().get().handle(event);	
	}
	
	protected void setVisible(Node element, boolean visible) {
		element.setVisible(visible);
		element.setManaged(visible);
	}
	
	/**
	 * 
	 */
	public AbstractRoutingControl(String view) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
		
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } 
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}


    private ObjectProperty<EventHandler<ActionEvent>> propertyOnConfigure = new SimpleObjectProperty<EventHandler<ActionEvent>>();
    private ObjectProperty<EventHandler<ActionEvent>> propertyOnCancel = new SimpleObjectProperty<EventHandler<ActionEvent>>();

    public final ObjectProperty<EventHandler<ActionEvent>> onConfigureProperty() {
        return propertyOnConfigure;
    }

    public final void setOnConfigure(EventHandler<ActionEvent> handler) {
    	propertyOnConfigure.set(handler);
    }

    public final EventHandler<ActionEvent> getOnConfigure() {
        return propertyOnConfigure.get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onCancelProperty() {
        return propertyOnCancel;
    }

    public final void setOnCancel(EventHandler<ActionEvent> handler) {
    	propertyOnCancel.set(handler);
    }

    public final EventHandler<ActionEvent> getOnCancel() {
        return propertyOnCancel.get();
    }
}
