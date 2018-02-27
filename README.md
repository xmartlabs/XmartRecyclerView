# XmartRecyclerView
<p align="center">
<a href="https://raw.githubusercontent.com/xmartlabs/XmartRecyclerView/master/LICENSE"><img src="http://img.shields.io/badge/license-MIT-blue.svg?style=flat" alt="License: MIT" /></a>
</p>

A smart, simple and fast RecyclerView library, made with ❤️ by [XMARTLABS](http://xmartlabs.com)

## Overview

XmartRecyclerView is a library created to make your life easier when using a RecyclerView. 
But it doesn't stop there. This library not only makes working with it a piece of cake, 
it also offers top-notch performance and a set of very interesting features that you will surely love.

## Contents

* [Loading On-Demand]
* [XmartAdapter]
* [Empty View Support]

### Loading On-Demand
XmartRecyclerView provides a way to set a threshold, so that when it's reached, it automatically loads the next
page of the RecyclerView. You only need to set that threshold, and decide what to load in the next page.
It's as easy as that.

### XmartAdapter
We know it's a struggle having to deal with duplicate items in a RecyclerView. That's why we made our adapter really
*xmart*. After you define when two objects are the actually same object, and when two objects have the same content, 
our adapter will automatically handle every addition to the list: if the item would be a duplicate, it just ignores 
its addition and keeps on working. The library makes use of Google's [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil.html) to be able to do this.

### Empty View Support
One of the not-so-minor problems when working with RecyclerViews is what to do when the list is simply *blank*.
We thought of that, so we offer what we like to call **Empty View Support**. Just decide an appropiate layout
that suits your needs, and set it as the empty view. XmartRecyclerView will handle the rest (yes, even if an item 
is removed and the list suddenly becomes empty, or if the list was empty but it now has an item, we will make that happen too).

## Getting involved

* If you **want to contribute** please feel free to **submit pull requests**.
* If you **have a feature request** please **open an issue**.
* If you **found a bug** check older issues before submitting a new one.
* If you **need help** or would like to **ask a general question**, use [StackOverflow]. (Tag `xmart-recyclerview`).

**Before contributing, please check the [CONTRIBUTING](CONTRIBUTING.md) file.**

[Loading On-Demand]: #loading-on-demand
[XmartAdapter]: #xmartadapter
[Empty View Support]: #empty-view-support
<!--- External -->
[StackOverflow]: http://stackoverflow.com/questions/tagged/xmart-recyclerview

## Donate to XmartRecyclerView

So we can make XmartRecyclerView even better!<br><br>
[<img src="donate.png"/>](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HRMAH7WZ4QQ8E)

## Changelog

The changelog for this project can be found in the [CHANGELOG](CHANGELOG.md) file.
