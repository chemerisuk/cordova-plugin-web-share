# cordova-plugin-web-share<br>[![NPM version][npm-version]][npm-url] [![NPM downloads][npm-downloads]][npm-url]
> Web Share API polyfill for Cordova

## Installation

    cordova plugin add cordova-plugin-web-share --save

## Supported Platforms

- iOS
- Android

## Methods

Plugin polyfills stadards-based [Web Share API](https://wicg.github.io/web-share/) to trigger native platform-specific dialogs. The first argument in `navigator.share` is an options object, that might have `title`, `text` and `url` properties (all optional).

```js
navigator.share({
    title: "Share topic",
    text: "Share message",
    url: "Share url"
}).then(() => {
    console.log("Data was shared successfully");
}).catch((err) => {
    console.error("Share failed:", err.message);
});
```

Method returns a `Promise` object that resolved on success.

Unlike the standard, that is web-oriented therefore must be very carefull on allowing access to private user information, apps are usually less restricted. So in the plugin returned promise resolves with a string value - package name of the app that user used to share:

```js
navigator.share({...}).then((packageName) => {
    if (packageName) {
        console.log("Data was shared successfully with", packageName);        
    }
}).catch((err) => {
    console.error("Share failed:", err.message);
});
```

[npm-url]: https://www.npmjs.com/package/cordova-plugin-web-share
[npm-version]: https://img.shields.io/npm/v/cordova-plugin-web-share.svg
[npm-downloads]: https://img.shields.io/npm/dm/cordova-plugin-web-share.svg

