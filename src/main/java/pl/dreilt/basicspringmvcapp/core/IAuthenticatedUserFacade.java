package pl.dreilt.basicspringmvcapp.core;

import pl.dreilt.basicspringmvcapp.user.AppUser;

public interface IAuthenticatedUserFacade {
    AppUser getAuthenticatedUser();
}
