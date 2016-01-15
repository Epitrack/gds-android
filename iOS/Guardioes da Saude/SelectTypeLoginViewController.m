//
//  SelectTypeLoginViewController.m
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 06/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import "SelectTypeLoginViewController.h"
#import "EnterViewController.h"
#import "User.h"
#import "HomeViewController.h"
#import "AFNetworking/AFNetworking.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "NoticeRequester.h"
#import "SingleNotice.h"
#import "TutorialViewController.h"
#import "UserRequester.h"
#import "MBProgressHUD.h"
#import "ViewUtil.h"

@interface SelectTypeLoginViewController () {
    
    User *user;
    SingleNotice *singleNotice;
    UserRequester *userRequester;
}

@end

@implementation SelectTypeLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    user = [User getInstance];
    singleNotice = [SingleNotice getInstance];
    userRequester = [[UserRequester alloc] init];
    
    // Do any additional setup after loading the view from its nib.
    self.navigationItem.title = @"";

    UIBarButtonItem *btnBack = [[UIBarButtonItem alloc]
                                initWithTitle:@""
                                style:UIBarButtonItemStylePlain
                                target:self
                                action:nil];
    
    self.navigationController.navigationBar.topItem.backBarButtonItem = btnBack;
    
    // Uncomment to automatically sign in the user.
    //GOOGLE
    GIDSignIn *signIn = [GIDSignIn sharedInstance];
    signIn.delegate = self;
    signIn.uiDelegate = self;
    signIn.clientID = @"783481918318-of721315npktlthk9fic2u02sp2psa9h.apps.googleusercontent.com";
    [signIn setScopes:[NSArray arrayWithObject: @"https://www.googleapis.com/auth/plus.login"]];
    [signIn setScopes:[NSArray arrayWithObject: @"https://www.googleapis.com/auth/plus.me"]];

    //FACEBOOK
    FBSDKLoginButton *btnFacebook = [[FBSDKLoginButton alloc] init];
    self.btnFacebook = btnFacebook;
    self.btnFacebook.readPermissions = @[@"public_profile", @"email", @"user_friends"];
    
    //TWITTER
    TWTRLogInButton* logInButton = [TWTRLogInButton buttonWithLogInCompletion:^(TWTRSession* session, NSError* error) {
        if (session) {
            NSLog(@"signed in as %@", [session userName]);
        } else {
            NSLog(@"error: %@", [error localizedDescription]);
        }
    }];
    
    self.btnTwitter = logInButton;    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)viewWillAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:animated];
    [super viewWillAppear:animated];
}

- (IBAction)btnLoginEmail:(id)sender {
    EnterViewController *enterViewController = [[EnterViewController alloc] init];
    [self.navigationController pushViewController:enterViewController animated:YES];
}

// Stop the UIActivityIndicatorView animation that was started when the user
// pressed the Sign In button

- (void)signIn:(GIDSignIn *)signIn
    didSignInForUser:(GIDGoogleUser *)userGL
     withError:(NSError *)error {
    if (!error) {
        [self checkSocialLoginWithToken:userGL.userID andType:GdsGoogle];
    }
}

- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
    // Perform any operations when the user disconnects from app here.
    // ...
}

- (IBAction)btnGoogleAction:(id)sender {
    
    //GOOGLE
    [[GIDSignIn sharedInstance] signIn];
}


//FACEBOOK
// Once the button is clicked, show the login dialog
- (IBAction)btnFacebookAction:(id)sender {
    
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login
     logInWithReadPermissions: @[@"public_profile", @"email"]
     fromViewController:self
     handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error) {
             UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Guardiões da Saúde" message:@"Erro ao logar com o Facebook." preferredStyle:UIAlertControllerStyleActionSheet];
             UIAlertAction *defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                 NSLog(@"You pressed button OK");
             }];
             [alert addAction:defaultAction];
             [self presentViewController:alert animated:YES completion:nil];
         } else if (result.isCancelled) {
             UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Guardiões da Saúde" message:@"Operação cancelada." preferredStyle:UIAlertControllerStyleActionSheet];
             UIAlertAction *defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                 NSLog(@"You pressed button OK");
             }];
             [alert addAction:defaultAction];
             [self presentViewController:alert animated:YES completion:nil];
         } else {
             if ([FBSDKAccessToken currentAccessToken])
             {
                 [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:nil]
                  startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id userFacebook, NSError *error)
                  {
                      NSLog(@"Logged in: %@", userFacebook);
                      if (!error)
                      {
                          NSDictionary *dictUser = (NSDictionary *)userFacebook;
                          
                          user.nick = dictUser[@"name"];
                          user.fb = dictUser[@"id"];
                          
                          [self checkSocialLoginWithToken:user.fb andType:GdsFacebook];
                      }
                  }];
             }
             
             
         }
     }];
}

- (IBAction)btnTwitterAction:(id)sender {
    
    [[Twitter sharedInstance] logInWithCompletion:^(TWTRSession *session, NSError *error) {
        if (session) {
            NSLog(@"signed in as %@", [session userName]);
            user.nick = [session userName];
            user.tw = [session userID];
            [self checkSocialLoginWithToken:user.tw andType: GdsTwitter];
        } else {
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Guardiões da Saúde" message:@"Erro ao logar com o Twitter." preferredStyle:UIAlertControllerStyleActionSheet];
            UIAlertAction *defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                NSLog(@"You pressed button OK");
            }];
            [alert addAction:defaultAction];
            [self presentViewController:alert animated:YES completion:nil];
        }
    }];
}

- (IBAction)iconBackAction:(id)sender {
    TutorialViewController *tutorialViewController = [[TutorialViewController alloc] init];
    [self.navigationController pushViewController:tutorialViewController animated:YES];
}

- (void) checkSocialLoginWithToken:(NSString *) token andType:(SocialNetwork)type {
    [userRequester checkSocialLoginWithToken:token
                                   andSocial:type
                                    andStart:^{
                                        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                                    }
                                andOnSuccess:^(User *userResponse){
                                    [MBProgressHUD hideHUDForView:self.view animated:YES];
                                    
                                    user = userResponse;
                                    NSUserDefaults *preferences = [NSUserDefaults standardUserDefaults];
                                    NSString *userKey = user.user_token;
                                    
                                    [preferences setValue:userKey forKey:@"userTokenKey"];
                                    [preferences synchronize];
                                    
                                    [self loadNotices];
                                    
                                    [self.navigationController pushViewController: [[HomeViewController alloc] init] animated: YES];
                                }
                                    andError:^(NSError *error){
                                        [MBProgressHUD hideHUDForView:self.view animated:YES];
                                        NSString *errorMsg;
                                        if (error) {
                                            errorMsg = @"Não existe cadastro com essa rede social.";
                                        } else {
                                            errorMsg = @"Ocorreu um erro de comunicação. Por favor, verifique sua conexão com a internet!";
                                        }
                                        
                                        [self presentViewController:[ViewUtil showAlertWithMessage:errorMsg] animated:YES completion:nil];
                                    }];
    
    
}

- (void) loginSocialWithEmail:(NSString *)email andPassword:(NSString *)password {
    
    NSString *url = @"http://api.guardioesdasaude.org/user/login";
    
    NSDictionary *params = @{@"email":email,
                             @"password":password};
    
    AFHTTPRequestOperationManager *manager;
    manager = [AFHTTPRequestOperationManager manager];
    [manager.requestSerializer setValue:user.app_token forHTTPHeaderField:@"app_token"];
    [manager POST:url
       parameters:params
          success:^(AFHTTPRequestOperation *operation, id responseObject) {
              
              if ([responseObject[@"error"] boolValue] == 1) {
                  UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Guardiões da Saúde" message:@"Não foi possível realizar a operação. Tente novamente em alguns minutos." preferredStyle:UIAlertControllerStyleActionSheet];
                  UIAlertAction *defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                      NSLog(@"You pressed button OK");
                  }];
                  [alert addAction:defaultAction];
                  [self presentViewController:alert animated:YES completion:nil];
              } else {
                  
                  NSDictionary *response = responseObject[@"user"];
                  
                  user.nick = response[@"nick"];
                  user.email = response[@"email"];
                  user.gender = response[@"gender"];
                  user.picture = response[@"picture"];
                  user.idUser =  response[@"id"];
                  user.race = response[@"race"];
                  user.dob = response[@"dob"];
                  user.user_token = response[@"token"];
                  user.hashtag = response[@"hashtags"];
                  user.household = response[@"household"];
                  user.survey = response[@"surveys"];
                  user.user_token = responseObject[@"token"];
                  
                  NSUserDefaults *preferences = [NSUserDefaults standardUserDefaults];
                  NSString *userKey = user.user_token;
                  
                  [preferences setValue:userKey forKey:@"userTokenKey"];
                  [preferences synchronize];
                  
                  [self loadNotices];
                  
                  [self.navigationController pushViewController: [[HomeViewController alloc] init] animated: YES];
              }
          } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
              NSLog(@"Error: %@", error);
              UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Guardiões da Saúde" message:@"Não foi possível realizar a operação. Tente novamente em alguns minutos." preferredStyle:UIAlertControllerStyleActionSheet];
              UIAlertAction *defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                  NSLog(@"You pressed button OK");
              }];
              [alert addAction:defaultAction];
              [self presentViewController:alert animated:YES completion:nil];
          }];
    
}

- (void) loadNotices {
    
    [[[NoticeRequester alloc] init] getNotices:user
                                       onStart:^{}
                                       onError:^(NSString * message){}
                                     onSuccess:^(NSMutableArray *noticesRequest){
                                         singleNotice.notices = noticesRequest;
                                     }];
}

@end
