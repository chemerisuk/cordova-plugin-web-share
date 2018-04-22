#import "ShareDialogPlugin.h"
#import <Cordova/CDV.h>

@implementation ShareDialogPlugin

- (void)share:(CDVInvokedUrlCommand*)command {
    NSDictionary* options = [command argumentAtIndex:0 withDefault:@{} andClass:[NSDictionary class]];
    NSMutableArray* activityItems = [[NSMutableArray alloc] init];

    if (options[@"text"]) {
        [activityItems addObject:options[@"text"]];
    }
    if (options[@"url"]) {
        [activityItems addObject:[NSURL URLWithString:options[@"url"]]];
    }

    if ([activityItems count] == 0) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } else {
        [self.commandDelegate runInBackground: ^{
            UIActivityViewController* dlg = [[UIActivityViewController alloc]
                                                  initWithActivityItems:activityItems
                                                  applicationActivities:NULL];

            dlg.excludedActivityTypes = options[@"iosExcludedActivities"];

            if (options[@"subject"]) {
                [dlg setValue:options[@"subject"] forKey:@"subject"];
            }

            UIPopoverPresentationController *popover = dlg.popoverPresentationController;
            if (popover) {
                popover.permittedArrowDirections = 0;
                popover.sourceView = self.webView.superview;
                popover.sourceRect = CGRectMake(CGRectGetMidX(self.webView.bounds), CGRectGetMidY(self.webView.bounds), 0, 0);
            }

            dispatch_async(dispatch_get_main_queue(), ^{
                dlg.completionWithItemsHandler = ^(NSString *activityType,
                                          BOOL completed,
                                          NSArray *returnedItems,
                                          NSError *error){
                    CDVPluginResult* pluginResult = NULL;
                    if (error) {
                        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
                    } else if (completed) {
                        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:activityType];
                    } else {
                        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@""];
                    }

                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                };

                [self.getTopPresentedViewController presentViewController:dlg animated:YES completion:NULL];
            });
        }];
    }
}

-(UIViewController *)getTopPresentedViewController {
    UIViewController *presentingViewController = self.viewController;
    while(presentingViewController.presentedViewController != nil && ![presentingViewController.presentedViewController isBeingDismissed])
    {
        presentingViewController = presentingViewController.presentedViewController;
    }
    return presentingViewController;
}

@end
