# cordova-plugin-web-share<br>[![NPM version][npm-version]][npm-url] [![NPM downloads][npm-downloads]][npm-url] [![Twitter][twitter-follow]][twitter-url]
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

Additionally to the standard, the plugin detects which activity was used to share. Resolved value is an array of selected activity names:

```js
navigator.share({...}).then((packageNames) => {
    if (packageNames.length > 0) {
        console.log("Shared successfully with activity", packageNames[0]);
    } else {
        console.log("Share was aborted");
    }
}).catch((err) => {
    console.error("Share failed:", err.message);
});
```

[npm-url]: https://www.npmjs.com/package/cordova-plugin-web-share
[npm-version]: https://img.shields.io/npm/v/cordova-plugin-web-share.svg
[npm-downloads]: https://img.shields.io/npm/dm/cordova-plugin-web-share.svg
[twitter-url]: https://twitter.com/chemerisuk
[twitter-follow]: https://img.shields.io/twitter/follow/chemerisuk.svg?style=social&label=Follow%20me
