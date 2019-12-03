package com.framework.common.config.jwt;

import org.apache.shiro.authc.UsernamePasswordToken;

public class JWTUserNamePasswordToken extends UsernamePasswordToken {
	
	private String authcToken ;

	public String getAuthcToken() {
		return authcToken;
	}

	public void setAuthcToken(String authcToken) {
		this.authcToken = authcToken;
	}

	public JWTUserNamePasswordToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, char[] password, boolean rememberMe) {
		super(username, password, rememberMe);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, char[] password, String host) {
		super(username, password, host);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, char[] password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}
	public JWTUserNamePasswordToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		// TODO Auto-generated constructor stub
	}
	public JWTUserNamePasswordToken(String username, String password, String token ,boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.authcToken = token;
		// TODO Auto-generated constructor stub
	}
	public JWTUserNamePasswordToken(String username, String password, boolean rememberMe) {
		super(username, password, rememberMe);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, String password, String host) {
		super(username, password, host);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String username, String password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}

	public JWTUserNamePasswordToken(String authorization) {
		this.authcToken = authorization;
	}

	
	
}
