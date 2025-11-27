/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ds.tools.hudson.crowd;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.atlassian.crowd.integration.acegi.user.CrowdUserDetails;
import hudson.tasks.UserNameResolver;

/**
 *
 * @author qxa4177
 */
@Extension
public class CrowdFullNameResolver extends UserNameResolver {

    @Override
    public String findNameFor(User u) {
        Hudson hudson = Hudson.getInstance();
        if (!(hudson.getSecurityRealm() instanceof CrowdSecurityRealm))
            return null;
        try {
            CrowdUserDetails details = (CrowdUserDetails) hudson.getSecurityRealm()
                    .getSecurityComponents().userDetails.loadUserByUsername(u.getId());
            String fullName = details.getFullName();
            if (fullName == null)
                return null; // not found
            return fullName;
        } catch (UsernameNotFoundException e) {
            LOGGER.log(Level.FINE, "Failed to look up Crowd for e-mail address", e);
            return null;
        } catch (DataAccessException e) {
            LOGGER.log(Level.FINE, "Failed to look up Crowd for e-mail address", e);
            return null;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(CrowdFullNameResolver.class.getName());
}
