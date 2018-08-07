curl \
-F "status=2" \
-F "notify=0" \
-F "ipa=@AppName/MVPExample/build/outputs/apk/MVPExample-dev-debug.apk" \
-H "X-HockeyAppToken: ${HOCKEYAPP_APP_ID}" \
https://rink.hockeyapp.net/api/2/apps/$AndroidAppId_Dev/app_versions/upload