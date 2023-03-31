package com.idea.recon.services;

import com.idea.recon.dtos.UserLoginDTO;

public interface TraineeLoginService {
	
    String validateUserLogin(Long Id, String password);
    
}
