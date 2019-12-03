package com.framework.modules.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.config.jwt.JWTUserNamePasswordToken;
import com.framework.common.utils.ConfigConstant;
import com.framework.common.utils.DESDecryptException;
import com.framework.common.utils.DESUtil;
import com.framework.common.utils.JWTUtil;
import com.framework.common.utils.R;
import com.framework.modules.sys.dao.SysUserDao;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.shiro.ShiroUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/sys/web-rsc")
//@Api(tags = "登录相关API")
public class SysWebResourceController extends AbstractController {
	@Autowired
	private Producer producer;
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
	}
	@Autowired
	private SysUserDao sysUserDao;
	
	@RequestMapping(value ={ "/sys/to_other_login_page"})
	public String toOtherLoginPage(String Authorization,HttpServletRequest request,HttpServletResponse resp) {
		System.out.println(Authorization);
		System.out.println(request.getParameter("Authorization"));
		return "";
	}
	/**
	 * 登录
	 */
	@ResponseBody//method = RequestMethod.POST
	@RequestMapping(value ={ "/sys/login"}, method = RequestMethod.POST)
	@ApiOperation("用户登录")
	@ApiImplicitParams(
			{@ApiImplicitParam(name="username",value="用户名",paramType="form",required=true,dataType="String"),
			@ApiImplicitParam(name="password",value="密码",paramType="form",required=true,dataType="String"),
			@ApiImplicitParam(name="captcha",value="验证码",paramType="form",required=false,dataType="String")})
	public R login(String username, String password, String captcha,HttpServletRequest req,HttpServletResponse resp) {
//		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//		if (!captcha.equalsIgnoreCase(kaptcha)) {
//			return R.error("验证码不正确");
//		}
		R r = R.ok();
		String decryptPWD = password;
		try {
			decryptPWD = DESUtil.decrypt(password);
		} catch (DESDecryptException e1) {
			decryptPWD  = password;
		}
		try {
			Subject subject = ShiroUtils.getSubject();
			SysUserEntity user = new SysUserEntity();
			user.setUsername(username);
			user = sysUserDao.selectOne(user);
			String authcToken = JWTUtil.sign(username, user.getPassword());
			JWTUserNamePasswordToken token = new JWTUserNamePasswordToken(username, decryptPWD);
			token.setAuthcToken(authcToken);
			subject.login(token);
			r.put("token", authcToken);
		} catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return R.error(ConfigConstant.LOGIN_CODE_INFO_ERROR,ConfigConstant.LOGIN_MSG_INFO_ERROR);
		} catch (LockedAccountException e) {
			return R.error(ConfigConstant.LOGIN_CODE_ACCOUNT_LOCKED,ConfigConstant.LOGIN_MSG_ACCOUNT_LOCKED);
		} catch (AuthenticationException e) {
			return R.error(ConfigConstant.LOGIN_CODE_CAPTCHA_FAILED,ConfigConstant.LOGIN_MSG_CAPTCHA_FAILED);
		}
		
		return r;
	}
	

	/**
	 * 退出:页面
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	/**
	 * 退出：接口
	 */
	@ResponseBody
	@RequestMapping(value = {"/sys/logout"}, method = RequestMethod.GET)
	@ApiOperation("用户登出")
	public R logoutRest() {
		try {
			ShiroUtils.logout();
		} catch (RuntimeException e) {
			return R.error(ConfigConstant.LOGIN_CODE_OTHER_ERROR,ConfigConstant.LOGIN_MSG_OTHER_ERROR);
		}
		return R.ok();
	}
}
