# TerminalSeekBar
[![](https://jitpack.io/v/alshell7/terminal-seekbar.svg)](https://jitpack.io/#alshell7/terminal-seekbar)
[![](http://img.shields.io/badge/build-passing-blue.svg)]()
[![](http://img.shields.io/badge/platform-android-green.svg)](https://developer.android.com/index.html)
[![](http://img.shields.io/badge/API-11+-brightgreen.svg)]()
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-TerminalSeekbar-orange.svg?style=flat)](https://android-arsenal.com/details/1/5956)

A smart seek bar for android with multiple junction points at random seek position.

<p align="left"><img src="https://github.com/alshell7/terminal-seekbar/blob/master/graphics/preview.gif"></p>

## USAGE

To use TerminalSeekBar in your layout XML, Add the library in your `Project build.gradle` :

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency in the `build.gradle (Module: app)` :

```groovy
dependencies {
	compile 'com.github.alshell7:terminal-seekbar:1.1'
}
```

### XML

```XML
    <com.projects.alshell.android.TerminalSeekBar
        android:layout_width="wrap_content"
        android:id="@+id/terminalSeekBar"
        android:layout_height="wrap_content"/>
```
You can use the following properties to change your TerminalSeekBar

#### Properties

* `app:bar_color`, format="color"
* `app:bar_height`, format="dimension"
* `app:thumb_height`, format="dimension"
* `app:current_value`, format="integer"
* `app:background_color`, format="color"
* `app:bar_margin`, format="dimension"
* `app:max_value`, format="integer"

### Java

```java
TerminalSeekBar terminalSeekBar = (TerminalSeekBar) findViewById(R.id.terminalSeekBar);

//Create a list of terminals to be set on the TerminalSeekBar
ArrayList<Terminal> terminals = new ArrayList<>();

terminals.add(new Terminal(10, Color.GREEN, "...this..." , Terminal.DEFAULT_PRIORITY_NORMAL));
terminals.add(new Terminal(100, Color.RED, "...HIGH...", Terminal.DEFAULT_PRIORITY_HIGH));
terminals.add(new Terminal(25, Color.BLUE, "...is...", Terminal.DEFAULT_PRIORITY_NORMAL));
terminals.add(new Terminal(70, Color.YELLOW, "...gettING...", Terminal.DEFAULT_PRIORITY_MEDIATE));
terminals.add(new Terminal(50, Color.GRAY, "...check...", Terminal.DEFAULT_PRIORITY_HIGH));

//Attach the terminals to the seek bar
terminalSeekBar.setTerminals(terminals);

//Change the animation of the terminals
terminalSeekBar.enablePriorityBlinking(TerminalAnimationType.BLINK_ACTIVE);

//Set event listeners of the TerminalSeekBar, or by implementing the methods in your Activity
//terminalSeekBar.setTerminalChangedListener(this);
terminalSeekBar.setTerminalChangedListener(new TerminalChangedListener()
{
	@Override
	public void onTerminalChanged(Terminal terminal)
	{
        	//Log.v(TAG, "Current Terminal : " + terminal.getInformation());
	}
});

terminalSeekBar.setSeekBarValueChangedListener(this); //Optional
```
#### Animations
The animations for `Terminal` are encapsulated in `TerminalAnimationType`.
```java
public enum TerminalAnimationType
{
    BLINK_STILL,
    BLINK_ACTIVE
}
```
### Demo

Install the [Demo](https://github.com/alshell7/terminal-seekbar/blob/master/app-debug.apk) app on your device for a complete view on library!

### Apps using library

* FollowApp - [Google Play Store](https://play.google.com/store/apps/details?id=com.products.zinnox.followapp)

## How to Contribute
1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -am 'Add some feature')
4. Push to the branch (git push origin my-new-feature)
5. Create new Pull Request

## License

```
Copyright 2017 alshell7

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
