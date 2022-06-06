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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	
        FXMLLoader loginLoader = new FXMLLoader(App.class.getResource("views/login.fxml"));
    	
        Scene loginScene = new Scene(loginLoader.load(), 370, 250);
        stage.setTitle("Login");
        stage.setScene(loginScene);

        LoginController loginController = loginLoader.getController();
        loginController.setOnLoginSuccess(session -> {
        	
        	try {
	        	FXMLLoader routingLoader = new FXMLLoader(App.class.getResource("views/routing.fxml"));
	        	routingLoader.setControllerFactory(param -> {
	        		return new RoutingController(session);
	        	});
	        	
	        	Scene routingScene = new Scene(routingLoader.load(), 420, 310);
	            stage.setTitle("Simple Routing");
	        	stage.setScene(routingScene);
	        	
	        	stage.setOnHidden(e -> {
	        		RoutingController routingController = routingLoader.getController();
	        		routingController.shutdown();
	        	});
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        	}
    	});
        
        stage.show();
    }
    
    
    public static void main(String[] args) {
    	System.setProperty("o2g.disable.ssl", "true");
        launch();
    }

}