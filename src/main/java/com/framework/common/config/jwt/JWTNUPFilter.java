package com.framework.common.config.jwt;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.common.utils.JWTUtil;
import com.framework.common.utils.SpringContextUtils;
import com.framework.modules.sys.dao.SysUserDao;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.service.SysDomainService;

public class JWTNUPFilter extends FormAuthenticationFilter {

	@Override
	public void setLoginUrl(String loginUrl) {
		// TODO Auto-generated method stub
		super.setLoginUrl(loginUrl);
	}

	@Override
	public String getUsernameParam() {
		// TODO Auto-generated method stub
		return super.getUsernameParam();
	}

	@Override
	public void setUsernameParam(String usernameParam) {
		// TODO Auto-generated method stub
		super.setUsernameParam(usernameParam);
	}

	@Override
	public String getPasswordParam() {
		// TODO Auto-generated method stub
		return super.getPasswordParam();
	}

	@Override
	public void setPasswordParam(String passwordParam) {
		// TODO Auto-generated method stub
		super.setPasswordParam(passwordParam);
	}

	@Override
	public String getRememberMeParam() {
		// TODO Auto-generated method stub
		return super.getRememberMeParam();
	}

	@Override
	public void setRememberMeParam(String rememberMeParam) {
		// TODO Auto-generated method stub
		super.setRememberMeParam(rememberMeParam);
	}

	@Override
	public String getFailureKeyAttribute() {
		// TODO Auto-generated method stub
		return super.getFailureKeyAttribute();
	}

	@Override
	public void setFailureKeyAttribute(String failureKeyAttribute) {
		// TODO Auto-generated method stub
		super.setFailureKeyAttribute(failureKeyAttribute);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.onAccessDenied(request, response);
	}

	@Override
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		return super.isLoginSubmission(request, response);
	}

	
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		return super.createToken(request, response);
	}
	
	@Override
	protected AuthenticationToken createToken(String username, String password, ServletRequest request,
			ServletResponse response) {
		// TODO Auto-generated method stub
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String token = getAuthcToken(request);
		return createToken(username, password, token,rememberMe,host);
	}
	
	private String getAuthcToken(ServletRequest request){
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getParameter("Authorization");
        return authorization;
	}
	
	@Override
	 protected AuthenticationToken createToken(String username, String password,
             boolean rememberMe, String host) {
		 return new JWTUserNamePasswordToken(username, password, rememberMe, host);
	 }
	 protected AuthenticationToken createToken(String username, String password,String authcToken,
             boolean rememberMe, String host) {
		 return new JWTUserNamePasswordToken(username, password, authcToken,rememberMe, host);
	 }
	@Override
	protected boolean isRememberMe(ServletRequest request) {
		// TODO Auto-generated method stub
		return super.isRememberMe(request);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		// TODO Auto-generated method stub
		return super.onLoginFailure(token, e, request, response);
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		// TODO Auto-generated method stub
		super.setFailureAttribute(request, ae);
	}

	@Override
	protected String getUsername(ServletRequest request) {
		// TODO Auto-generated method stub
		return super.getUsername(request);
	}

	@Override
	protected String getPassword(ServletRequest request) {
		// TODO Auto-generated method stub
//		org.apache.shiro.web.filter.authc.FormAuthenticationFilter

		return super.getPassword(request);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getParameter("Authorization");
        if(authorization!=null) {
        	try {
				AuthenticationToken token = new JWTUserNamePasswordToken(authorization);
				// 提交给realm进行登入，如果错误他会抛出异常并被捕获
				getSubject(request, response).login(token);
			} catch (AuthenticationException e) {
				saveRequest(request);
				return false;
			}
            
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        }
		return super.isAccessAllowed(request, response, mappedValue);
	}
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		String authorization = request.getParameter("Authorization");
		if(authorization!=null){
			SysDomainService sds = (SysDomainService)SpringContextUtils.getBean(SysDomainService.class);
			SysUserDao sysUserDao = (SysUserDao)SpringContextUtils.getBean(SysUserDao.class);
			String username = JWTUtil.getUsername(authorization);
			SysUserEntity user = new SysUserEntity();
		 	user.setUsername(username);
		 	user = sysUserDao.selectOne(user);
		 	String loginUrl = "/login.html";
		 	if(user.getDomainId()!=null){
				SysDomainEntity sde = sds.selectById(user.getDomainId());
				if(sde!=null){
					if(sde.getBasePath()!=null){
						loginUrl = sde.getBasePath();
					}
				}
		 	}
			WebUtils.issueRedirect(request, response, loginUrl);
		}else{
			super.redirectToLogin(request, response);
		}
	}

	/**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletRequest req, ServletResponse resp,String errorMsg) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.getWriter().write(errorMsg);
//            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

}
