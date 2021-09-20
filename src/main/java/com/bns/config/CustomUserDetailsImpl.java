package com.bns.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bns.entites.User;

import lombok.NoArgsConstructor;
@NoArgsConstructor
public class CustomUserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;

	public CustomUserDetailsImpl(User user) {
		super();
		this.user = user;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());

		List roleList = new ArrayList();
		roleList.add(grantedAuthority);
		return roleList;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
