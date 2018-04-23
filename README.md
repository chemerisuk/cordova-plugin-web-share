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

Method returns a `Promise` object, that resolves with a string containing the package name of app, that user used to. While the standards prohibits any collection personal information, native apps are less restricted.

[npm-url]: https://www.npmjs.com/package/cordova-plugin-web-share
[npm-version]: https://img.shields.io/npm/v/cordova-plugin-web-share.svg
[npm-downloads]: https://img.shields.io/npm/dm/cordova-plugin-web-share.svg

