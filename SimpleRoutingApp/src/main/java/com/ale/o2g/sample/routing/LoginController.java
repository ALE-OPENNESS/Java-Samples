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
import java.io.Reader;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ale.o2g.O2G;
import com.ale.o2g.ServiceEndPoint;
import com.ale.o2g.Session;
import com.ale.o2g.sample.routing.util.CipherUtil;
import com.ale.o2g.types.Credential;
import com.ale.o2g.types.Host;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginController {

	private static class LoginData {
		
		private String serverAdddress;
		private String login;
		private String password;
				
		public LoginData(String serverAddress, String login, String password) {
			this.serverAdddress = serverAddress;
			this.login = login;
			this.password = CipherUtil.get().encrypt(password);
		}

		public String getServerAdddress() {
			return serverAdddress;
		}

		public String getLogin() {
			return login;
		}

		public String getPassword() {
			return CipherUtil.get().decrypt(password);
		}
	};
	
	private static class JsonConfigFile {

		private static final String CONFIG_FILENAME = ".routingApp.json";
		
		private static Path getFilename() {
			return FileSystems.getDefault().getPath(System.getProperty("user.home"), CONFIG_FILENAME);
		}
		
		public static LoginData load() {
			
			try {
				Gson gson = new Gson();
				
				Reader reader = Files.newBufferedReader(getFilename());
				LoginData data = gson.fromJson(reader, LoginData.class);
				reader.close();
	
				return data;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static void save(LoginData data) {
			
			try {
				Gson gson = new Gson();
				
				Writer writer = Files.newBufferedWriter(getFilename());
			    gson.toJson(data, writer);
			    writer.close();
			} 
			catch (Exception e) {
				// Do nothing just signal an exception
				e.printStackTrace();
			} 			
		}

	}	
	
		
	@FXML private TextField tfServerAddress;
	@FXML private TextField tfLogin;
	@FXML private PasswordField tfPassword;
	@FXML private Button btLogin;	
	@FXML private CheckBox cbRemember;
	@FXML private Text txtError;
	
	private LoginSuccessHandler loginSuccessHandler = null;

	/**
	 * Enable the login button
	 */
	private void enableLoginButton() {
		btLogin.setDisable(
    			tfServerAddress.getText().isBlank() ||
    			tfLogin.getText().isBlank() ||
    			tfPassword.getText().isBlank()
    			);
	}
	
	/**
	 * Set a login handler
	 * @param h
	 */
	public void setOnLoginSuccess(LoginSuccessHandler h) {
		this.loginSuccessHandler = h;
	}
	
	
	@FXML
    private void initialize() {
		LoginData data = JsonConfigFile.load();
		if (data != null) {
			this.tfServerAddress.setText(data.getServerAdddress());
			this.tfLogin.setText(data.getLogin());
			this.tfPassword.setText(data.getPassword());
		}
		enableLoginButton();
    }	
	
    @FXML
    private void enableLoginButton(KeyEvent event) throws IOException {
    	enableLoginButton();
    }
    
    @FXML
    private void cancelAction(ActionEvent event) throws IOException {
    	Platform.exit();
    }
    
    @FXML
    private void loginAction(ActionEvent event) throws IOException {

		if (this.cbRemember.isSelected()) {
			// Store information in file
			JsonConfigFile.save(new LoginData(
					tfServerAddress.getText(),
					tfLogin.getText(), 
					tfPassword.getText()));
		}    		

		final Task<Session> task = new Task<Session>() {

			@Override
			protected Session call() throws Exception {
				ServiceEndPoint o2gServer = O2G.Connect(new Host(tfServerAddress.getText()));
				return o2gServer.openSession(new Credential(tfLogin.getText(), tfPassword.getText()), "RoutingApplication");
			}
		};
		
		task.setOnRunning((e) -> {
			txtError.setText("Connecting ... please wait");
			txtError.setFill(Color.BLUE);
		});
		
		task.setOnSucceeded((e) -> {
			if (this.loginSuccessHandler != null) {
				this.loginSuccessHandler.handle(task.getValue());
			}
		});
		
		
		task.setOnFailed((e) -> {
			txtError.setText("Unable to open session");
			txtError.setFill(Color.RED);

			Throwable exception = task.getException();
			if (exception != null) {
				exception.printStackTrace();
			}
		});
		
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		executorService.execute(task);
		executorService.shutdown();
    }
    
}
