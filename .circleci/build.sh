#!/bin/sh
#/usr/libexec/PlistBuddy -c "Set :CFBundleVersion $CIRCLE_BUILD_NUM" "yourappname/Info.plist"
ipa build --verbose -c Release