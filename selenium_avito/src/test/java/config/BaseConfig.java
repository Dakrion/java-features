package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/config.properties")
public interface BaseConfig extends Config {
    String avitoUrl();
    String chromeDriver();
}
