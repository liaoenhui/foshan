package com.framework.common.config.jwt.cm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.framework.common.config.jwt.JWTUserNamePasswordToken;
import com.framework.common.utils.JWTUtil;
import com.framework.modules.sys.entity.SysUserEntity;

public class AuthcTokenCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	protected Object getCredentials(AuthenticationToken token) {
		// TODO Auto-generated method stub
		return super.getCredentials(token);
	}

	@Override
	protected Object getCredentials(AuthenticationInfo info) {
		// TODO Auto-generated method stub
		return super.getCredentials(info);
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// TODO Auto-generated method stub
		JWTUserNamePasswordToken jpToken = (JWTUserNamePasswordToken) token;
		String t = jpToken.getAuthcToken();
		SysUserEntity user =  (SysUserEntity) info.getPrincipals().getPrimaryPrincipal();
		return JWTUtil.verify(t, user.getUsername(), user.getPassword());
//		return super.doCredentialsMatch(token, info);
	}

}
