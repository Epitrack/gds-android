//
//  SelectTypeCreateAccoutViewController.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 20/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Google/SignIn.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <TwitterKit/TwitterKit.h>

@interface SelectTypeCreateAccoutViewController : UIViewController <GIDSignInUIDelegate>
@property (weak, nonatomic) IBOutlet FBSDKLoginButton *btnFacebook;
@property (weak, nonatomic) IBOutlet GIDSignInButton *btnGoogle;
@property (weak, nonatomic) IBOutlet TWTRLogInButton *btnTwitter;
@property (weak, nonatomic) IBOutlet UIButton *btnEmail;
- (IBAction)btnFacebookAction:(id)sender;
- (IBAction)btnGoogleAction:(id)sender;
- (IBAction)btnTwitterAction:(id)sender;
- (IBAction)btnEmailAction:(id)sender;

@end