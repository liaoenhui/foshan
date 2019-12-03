package com.framework.modules.sys.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.framework.common.config.jwt.JWTUserNamePasswordToken;
import com.framework.common.config.jwt.cm.AuthcTokenCredentialsMatcher;
import com.framework.common.utils.Constant;
import com.framework.common.utils.JWTUtil;
import com.framework.modules.sys.dao.SysMenuDao;
import com.framework.modules.sys.dao.SysUserDao;
import com.framework.modules.sys.entity.SysMenuEntity;
import com.framework.modules.sys.entity.SysUserEntity;

@Component
public class JWTRealm extends AuthorizingRealm {

//    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysMenuDao sysMenuDao;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTUserNamePasswordToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
		String userId = user.getUserId();

		List<String> permsList;

		// 系统管理员，拥有最高权限
		if ( Constant.SUPER_ADMIN.contentEquals(userId)) {
			List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
			permsList = new ArrayList<>(menuList.size());
			for (SysMenuEntity menu : menuList) {
				permsList.add(menu.getPerms());
			}
		} else {
			permsList = sysUserDao.queryAllPerms(userId);
		}

		// 用户权限列表
		Set<String> permsSet = new HashSet<>();
		for (String perms : permsList) {
			if (StringUtils.isBlank(perms)) {
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
    }
    private SysUserEntity getUser(String username){
    	 SysUserEntity user = new SysUserEntity();
 		user.setUsername(username);
 		user = sysUserDao.selectOne(user);
 		return user;
    }
    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    	JWTUserNamePasswordToken jpToken = (JWTUserNamePasswordToken) auth;
        String token = (String) jpToken.getAuthcToken();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("无效Token");
        }
        SysUserEntity user = getUser(username);

        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }

//        if (! JWTUtil.verify(token, username, user.getPassword())) {
//            throw new AuthenticationException("用户密码错误");
//        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }
    @Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
    	AuthcTokenCredentialsMatcher atcm = new AuthcTokenCredentialsMatcher();
    	super.setCredentialsMatcher(atcm);
	}
}
