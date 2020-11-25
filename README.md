## CPower Controller

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](https://github.com/vn7n24fzkq/CPower-Controller/blob/master/LICENSE)
![release](https://img.shields.io/github/v/release/vn7n24fzkq/CPower-Controller?style=for-the-badge)

Because there many problem by using CP5200.dll for me, so I write this lib to control CPower LED display by implement the protocol.

However, it is found that the TCP/IP instant messaging protocol is different from the document, and when you implement other parts of the protocol, you should be noted that there may be the same problem.

Only implement instant message function.

因為 CP5200.dll 使用上有許多問題所以寫了這個 lib 來發送 instant message

但發現 TCP/IP instant message protocol 與文件上不一樣, 實作其他部分應注意可能有一樣的問題

只有實作 instant message 的功能 其他功能並未實作

## Quickstart

### Add dependency

#### Maven

```xml
<dependency>
	<groupId>vn7.cpower-controller</groupId>
	<artifactId>cpower-controller</artifactId>
	<version>1.2.1</version>
	<type>pom</type>
</dependency>
```

#### Gradle

```groovy
implementation 'vn7.cpower-controller:cpower-controller:1.2.1'
```

### Ivy

```ivy
<dependency org="vn7.cpower-controller" name="cpower-controller" rev="1.2.1">
	<artifact name="cpower-controller" ext="pom"></artifact>
</dependency>
```

### Example

```java

// create a controller object
CPowerController cPowerController = new CPowerController();
cPowerController.connect("192.168.1.222", 5200);

// create instant message
InstantMessage instantMessage = new InstantMessage();
instantMessage.setText("Hello world");
instantMessage.setColor(true,false,false);
instantMessage.setEffect(InstantMessageEffect.Draw);
instantMessage.setFontSize((byte)0x02);
instantMessage.setHeight((short)32);
instantMessage.setWidth((short)32);
instantMessage.setLoopTime((short) 3);

// send data to CPower
cPowerController.send(instantMessage);

// disconnect
cPowerController.disconnect();
```
