<div align="center">

<img src="https://raw.githubusercontent.com/pixelexplosion/ppc/main/logo/ppc-logo.svg" width="350px"  alt="ppc-logo"/>

# PPC
  

<img src="https://img.shields.io/badge/OS-CROSSPLATFORM-13C6FF?style=for-the-badge" alt="cross-platform">
<img src="https://img.shields.io/github/stars/pixelexplosion/ppc?color=13C6FF&logo=github&logoColor=13C6FF&style=for-the-badge" alt="stars">


PPC is a simple note-taking application focused on productivity. The app works on Windows, Linux and Mac.
Note: This project is still work in progress. At this point in time features and file formats might change rapidly.
<div align="left">

## Getting Started
To run PPC make sure to clone the latest version of this repository, and you have a working installation of gradle on your computer. Inside the project directory execute:
```
gradle run
```
If you're executing PPC for the first time this might take a while getting all the dependencies. Further executions should be done within a few seconds.
## Roadmap
✔️ Complete •
🪛 Work in Progress •
❌ Not supported yet

---

✔️ Basic drawing

✔️ Stroke smoothing with splines

✔️ Stroke eraser

✔️ Selection tool

✔️ Scrolling and zooming

✔️ Pages

✔️ Saving Documents

✔️ Loading Documents from an integrated file tree

🪛 Undo and redo

🪛 Page types

🪛 Custom pens

🪛 Infinite canvas

🪛 Shape recognition

🪛 Themes

🪛 Settings

❌ PDF export

❌ PDF import

❌ Multitouch support (not supported by UI-framework yet)

❌ Stylus pressure support (not supported by UI-framework yet)

## Technical Details
PPC is built in <a href="https://github.com/JetBrains/kotlin">Kotlin</a> using the UI-framework <a href="https://github.com/JetBrains/compose-jb">Compose for Desktop</a>. PPC is rendering on Compose for Desktop's builtin canvas, which internally uses the fast rendering engine <a href="https://github.com/google/skia">skia</a>.
PPC uses for most of the part MVC as its software architecture.
For stroke smoothing, points are sampled from the userinput and a <a href="https://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline">Catmull-Rom splines</a> is fitted through them.

## Contributors
<a href="https://github.com/larsvommars">
LarsVomMars
</a>and<a href="github.com/AnyUnderstanding">
AnyUnderstanding
</a>


