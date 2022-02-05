<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta name="generator" content="pandoc" />
  <title>Serval Help: Release Notes</title>
  <style type="text/css">code{white-space: pre;}</style>
  <link rel="stylesheet" href="servalhelp.css" type="text/css" />
  <meta name="description" content="Serval App WebView Page" />
</head>
<body>
<h1 id="release-notes-for-serval-mesh-0.93">Release Notes for Serval Mesh 0.93</h1>
<p><a href="http://www.servalproject.org/">Serval Project</a>, May 2016</p>
<p>These notes accompany the release in April 2016 of version 0.93 of the <a href="https://play.google.com/store/apps/details?id=org.servalproject">Serval Mesh</a> app for <a href="http://developer.android.com/about/versions/android-2.2-highlights.html">Android 2.2 “Froyo”</a> and above.</p>
<h2 id="what-is-serval-mesh">What is Serval Mesh?</h2>
<p>Serval Mesh is an app for <a href="http://developer.android.com/about/versions/android-2.2-highlights.html">Android 2.2 “Froyo”</a> and above. It provides free, secure phone-to-phone voice calling, SMS and file sharing over <a href="http://en.wikipedia.org/wiki/Wi-Fi">Wi-Fi</a> or [Bluetooth][], without the need for a SIM card or a commercial mobile telephone carrier. In other words, it lets your Android phone call other Android phones running Serval Mesh within Wi-Fi range.</p>
<p>The <a href="./PRIVACY.md">Serval Mesh Privacy Policy</a> describes how Serval Mesh handles your personal and other sensitive information.</p>
<h2 id="warnings">Warnings</h2>
<p>Serval Mesh is <strong>EXPERIMENTAL SOFTWARE</strong>. It has not yet reached version 1.0, and is intended for pre-production, demonstration purposes only. It may not work as advertised, it may lose or alter messages and files that it carries, it may consume a lot of space, speed and battery, and it may crash unexpectedly.</p>
<p>On the Serval Mesh &quot;Connect&quot; screen, connecting to &quot;Ad Hoc Mesh&quot; will request <a href="http://en.wikipedia.org/wiki/Android_rooting">root permission</a> (super-user) on your Android device in order to put Wi-Fi into <a href="http://compnetworking.about.com/cs/wirelessfaqs/f/adhocwireless.htm">Ad-Hoc mode</a>. If you grant super-user permission to Serval Mesh, it will attempt to reinstall the Wi-Fi driver software on your device, which <strong>could result in YOUR DEVICE BECOMING PERMANENTLY DISABLED (&quot;BRICKED&quot;).</strong></p>
<p>On the Serval Mesh &quot;Connect&quot; screen, selecting &quot;Portable Wi-Fi Hotspot&quot; will put your device's Wi-Fi into <a href="http://compnetworking.about.com/cs/wireless/g/bldef_ap.htm">Access Point mode</a>. If you have a mobile data plan, <strong>this will give nearby devices access to your mobile data plan, and COULD COST YOU MONEY.</strong></p>
<p>The Serval Mesh &quot;Connect&quot; screen allows you to connect to other Serval Mesh devices that act as Access Points (Hotspots) or Ad Hoc peers. If you do so, <strong>this will cut off normal Wi-Fi network access</strong> while Serval Mesh is running, and services like Google Updates, E-mail, social media and other notifications may not work.</p>
<p>Serval Mesh telephony is a “best effort” service, primarily intended for when conventional telephony is not possible or cost effective, and <strong>MUST NOT BE RELIED UPON</strong> for emergencies in place of carrier-grade communications systems. The Serval Project cannot be held responsible for any performance or non-performance of the technologies that they provide in good will, and if you use these technologies you must agree to indemnify the Serval Project from any such claims.</p>
<p>The Serval Mesh software copies all files shared using the <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:rhizome">Rhizome</a> file distribution service to other phones and devices running the Serval Mesh software, regardless of size, content or intended recipient. The Serval Project cannot be held responsible for the legality or propriety of any files received via Rhizome, nor for any loss, damage or offence caused by the transmission or receipt of any content via Rhizome.</p>
<p>See the disclaimers below.</p>
<h2 id="whats-new-since-0.92">What's new since 0.92</h2>
<ul>
<li><p>Greatly reduced power usage, particularly when no peers are present. In previous versions of the software, a CPU lock would be held whenever the software was enabled and connected to a viable Wi-Fi network. This would completely prevent the CPU from suspending, draining the battery in a matter of hours. In this release, Android alarms are used to wake up the CPU, holding a CPU lock for only a short time. While there are still improvements to be made in this area, the software may be able to remain enabled and connected to a Wi-Fi network without significantly impacting battery life.</p></li>
<li><p>Bluetooth has been added as a usable network transport. The addition of bluetooth support has the potential to greatly simplify the process of discovering and connecting to other phones.</p></li>
<li><p>Better support for more recent versions of Android. Android 5.0 requires that native binaries are compiled in a way that isn't supported on version before 4.1. So we must now include 2 sets of compiled binaries.</p></li>
<li><p>Improved user feedback while networks are turning on and off.</p></li>
</ul>
<h2 id="supported-devices">Supported Devices</h2>
<p>This release of Serval Mesh has been tested on a variety of Android devices, and is expected to run on almost any Android phone. It does NOT require root.</p>
<p>Prior releases of Serval Mesh are known to work on the following devices, which is included for historical purposes, as many of the following devices support ad-hoc Wi-Fi mode for those for whom that is interesting:</p>
<ul>
<li><p><strong>Huawei IDEOS X1 u8180</strong>, running Android 2.2.2 (rooted) and CyanogenMod 2.3.7</p></li>
<li><p><strong>HTC Sensation</strong>, running Android 2.3.4 (rooted) and HTC Sense 3.0</p></li>
<li><p><strong>HTC One S</strong></p></li>
<li><p><strong>Motorola Milestone</strong></p></li>
<li><p><strong>Huawei IDEOS u8150</strong></p></li>
<li><p><strong>Samsung Galaxy Tab 7 inch</strong></p></li>
<li><p><strong>Samsung Galaxy Gio S5660</strong>, running Android 2.3.6 (rooted)</p></li>
<li><p><strong>Samsung Vitality SCH-R720</strong></p></li>
<li><p><strong>ZTE Score X500</strong></p></li>
<li><p><strong>HTC/Google G1</strong> (“Dream”)</p></li>
</ul>
<p>Previous releases of Serval Mesh were known to work on the following devices with minor problems:</p>
<ul>
<li><p><strong>Samsung Galaxy S2 GT-I9100</strong>, running Android 2.3 (rooted): Ad-Hoc Wi-Fi is not completely compatible with the Ad-Hoc Wi-Fi on other devices, specifically the Huawei IDEOS phones listed above. If the Galaxy S2 is the first device to join the mesh, then IDEOS phones cannot join. However, if an IDEOS phone is the first device, then the Galaxy S2 <em>does</em> join okay.</p></li>
<li><p><strong>Google Nexus 1</strong>: does not interoperate well with HTC/Google G1.</p></li>
</ul>
<p>The following devices have major known problems when attempting to use ad-hoc Wi-Fi in this or prior releases:</p>
<ul>
<li><p>HTC Wildfire A3335</p></li>
<li><p>Samsung Galaxy Nexus: Wi-Fi Ad-Hoc mode does not start; Wi-Fi mode reverts to Off.</p></li>
<li><p>Motorola Razr i XT890: Wi-Fi control does not work.</p></li>
<li><p>Samsung Galaxy Note 2: does not detect peers. Possibly the same problem as the Galaxy S2 described above, but not tested.</p></li>
</ul>
<p>See the <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:hardware:devices">Mobile Device Compatability Table</a> for more details and devices.</p>
<h2 id="known-issues">Known Issues</h2>
<ul>
<li><p>While Serval Mesh services are enabled and you are connected to a Wi-Fi network with active peers, Android may be prevented from sleeping. This may drain the battery quickly -- see <a href="https://github.com/servalproject/batphone/issues/91">batphone issue #91</a>.</p></li>
<li><p>Voice call quality degrades whenever <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:rhizome">Rhizome</a> or <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:meshms">MeshMS</a> operations or transfers are in progress. <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:rhizome">Rhizome</a> can worsen network congestion, transfers are not throttled and can lead to additional network latency and packet loss due to a problem known as <a href="http://en.wikipedia.org/wiki/Bufferbloat">Bufferbloat</a>. -- see <a href="https://github.com/servalproject/serval-dna/issues/1">serval-dna issue #1</a>.</p></li>
<li><p>Voice call quality is variable. We try to enable echo cancellation, if supported by the handset. However some echo may have to be controlled by lowering speaker volume or using earphones. Audio latency (delay) can exceed one second in some situations -- see <a href="https://github.com/servalproject/batphone/issues/93">batphone issue #93</a>.</p></li>
<li><p>Voice call audio has been observed to be missing on a Nexus 4 running 4.2.1, and upgrading to a 4.2.2 custom ROM restored audio -- see <a href="https://github.com/servalproject/batphone/issues/77">batphone issue #77</a> and <a href="https://github.com/servalproject/batphone/issues/96">batphone issue #96</a>.</p></li>
<li><p>VoMP does not play a &quot;ringing&quot; sound while placing a call, nor a &quot;hangup&quot; sound when the other party hangs up, nor any other indicator of networking failures -- see <a href="https://github.com/servalproject/batphone/issues/76">batphone issue #76</a>.</p></li>
<li><p>Every new <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:meshms">MeshMS</a> message increases the size of the <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:rhizome">Rhizome</a> payload that contains all the messages in that conversation ply. So every <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:tech:meshms">MeshMS</a> conversation will consume more network bandwidth and SD Card space as it grows -- see <a href="https://github.com/servalproject/serval-dna/issues/28">serval-dna issue #28</a>. This cannot be worked around.</p></li>
<li><p>If a user starts a Serval Hotspot on the &quot;Connect&quot; screen, then the application overwrites the user's own personal hotspot name (and settings) with &quot;ap.servalproject.org&quot;. When the Serval Hotspot is turned off, Serval Mesh restores the user's own personal hotspot settings, which involves turning the user's Wi-Fi hotspot on and off briefly. This could cause some concern or confusion, but is the only way that Android provides to restore hotspot settings.</p></li>
</ul>
<p>There are more known bugs and issues listed under the GitHub Issues page for <a href="https://github.com/servalproject/batphone/issues">batphone issues</a> and <a href="https://github.com/servalproject/serval-dna/issues">serval-dna issues</a>.</p>
<h2 id="copyright-and-licensing">Copyright and licensing</h2>
<p>Serval Mesh is <a href="http://www.gnu.org/philosophy/free-sw.html">free software</a> produced by the <a href="http://www.servalproject.org/">Serval Project</a> and many <a href="./CREDITS.md">contributors</a>. The copyright in all source code is owned by Serval Project Inc., an organisation incorporated in the state of South Australia in the Commonwealth of Australia.</p>
<p>The Java/XML source code of Serval Mesh is licensed to the public under the <a href="./LICENSE-SOFTWARE.md">GNU General Public License version 3</a>. The <a href="https://github.com/servalproject/serval-dna">serval-dna</a> component of Serval Mesh is licensed to the public under the <a href="http://www.gnu.org/licenses/gpl-2.0.html">GNU General Public License version 2</a>.</p>
<p>All <a href="http://developer.servalproject.org/dokuwiki/doku.php?id=content:dev:techdoc">technical documentation</a> is licensed to the public under the <a href="./LICENSE-DOCUMENTATION.md">Creative Commons Attribution 4.0 International license</a>.</p>
<p>All source code and technical documentation is freely available from the Serval Project's <a href="https://github.com/servalproject/batphone">batphone</a> and <a href="https://github.com/servalproject/serval-dna">serval-dna</a> Git repositories on <a href="https://github.com/servalproject">GitHub</a>.</p>
<h2 id="acknowledgements">Acknowledgements</h2>
<p>This release would not have been possible without the support of <a href="http://www.usaid.gov/">United States Agency for International Development</a> and <a href="http://www.rfa.org/">Radio Free Asia</a>.</p>
<p>Earlier development of Serval Mesh has been funded by the <a href="http://www.newamerica.net/">New America Foundation's</a> <a href="http://oti.newamerica.net/">Open Technology Institute</a>, the <a href="http://www.shuttleworthfoundation.org/">Shuttleworth Foundation</a>, <a href="http://www.nlnet.nl/">Nlnet Foundation</a>, <a href="http://www.openitp.org/">OpenITP</a>, and our &quot;True Believers&quot;:</p>
<ul>
<li>Douglas P. Chamberlin</li>
<li>Walter Ebert</li>
<li>Andrew G. Morgan, California, USA</li>
<li>Fred Fisher</li>
</ul>
<p>The Serval Project was founded by <a href="http://www.flinders.edu.au/people/paul.gardner-stephen">Dr Paul Gardner-Stephen</a> and <a href="http://www.flinders.edu.au/people/romana.challans">Romana Challans</a>, both academic staff at the <a href="http://www.flinders.edu.au/science_engineering/csem/">School of Computer Science, Engineering and Mathematics</a> at <a href="http://www.flinders.edu.au/">Flinders University</a> in South Australia. Their work on the Serval Project is made possible by the ongoing support of the university.</p>
<h2 id="disclaimer">Disclaimer</h2>
<p>SERVAL MESH refers to the software, protocols, systems and other goods, tangible and intangible produced by The Serval Project, Serval Project, Inc., and Serval Project Pty Limited.</p>
<p>SERVAL MESH COMES WITH NO WARRANTY, EXPRESSED OR IMPLIED, AND IS NOT FIT FOR MERCHANTABILITY FOR ANY PURPOSE. USE AT YOUR SOLE RISK.</p>
<p>SERVAL MESH WILL REDUCE THE BATTERY LIFE OF DEVICES ON WHICH IT RUNS.</p>
<p>SERVAL MESH MAY CONSUME ALL STORAGE, both LOCAL and EXTERNAL (eg, MICRO SD CARD) ON THE DEVICES ON WHICH IT RUNS.</p>
<p>SERVAL MESH SHOULD NOT BE INSTALLED ON DEVICES WHICH ARE DEPENDED UPON FOR EMERGENCY COMMUNICATION.</p>
<p>SERVAL MESH MAY TRANSMIT SOME DATA IN THE CLEAR.</p>
<p>SERVAL MESH PROTECTIONS against IMPERSONATION or OTHER MISAPPROPRIATION of IDENTITY ESTABLISHING FACTORS MAY BE DEFECTIVE and MAY NOT PERFORM AS EXPECTED.</p>
<p>SERVAL MESH SHOULD NOT BE RELIED UPON IN AN EMERGENCY is it is an INCOMPLETE PROTOTYPE and BEST EFFORT in nature, and may FAIL TO OPERATE.</p>
<p>SERVAL MESH may COST YOU MONEY if you have a MOBILE DATA PLAN by TURNING OFF WI-FI NETWORK ACCESS or by allowing NEARBY DEVICES TO USE YOUR DATA PLAN WITHOUT YOUR KNOWLEDGE OR CONSENT.</p>
<p>SERVAL MESH may REVEAL AND/OR BROADCAST YOUR LOCATION, IDENTITY OR OTHER INFORMATION through its normal operation.</p>
<p>SERVAL MESH is an INCOMPLETE, PRE-PRODUCTION software, experimental in nature and is not to be considered fit for merchantability for any purpose. It has many defects, omissions and errors that will hamper its fulfilling of its intended purposes.</p>
<hr />
<p><strong>Copyright 2016 Serval Project Inc.</strong><br />
<img src="./cc-by-4.0.png" alt="CC-BY-4.0" /> This document is available under the <a href="./LICENSE-DOCUMENTATION.md">Creative Commons Attribution 4.0 International licence</a>.</p>
</body>
</html>
