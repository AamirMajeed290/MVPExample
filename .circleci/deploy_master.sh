#!/usr/bin/env bash
curl \
-F "status=2" \
-F "notify=0" \
-F "ipa=MVPExample/build/outputs/apk/MVPExample-dev-debug.apk" \
-H "X-HockeyAppToken: 9f8d07d7acb04e84b3f292b62b8ac0e0" \
https://rink.hockeyapp.net/api/2/apps/freeWifi/app_versions/upload