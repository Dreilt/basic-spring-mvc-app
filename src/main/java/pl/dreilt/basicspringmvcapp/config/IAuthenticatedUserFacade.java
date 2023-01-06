package pl.dreilt.basicspringmvcapp.config;

import pl.dreilt.basicspringmvcapp.entity.AppUser;

public interface IAuthenticatedUserFacade {
    AppUser getAuthenticatedUser();
}
