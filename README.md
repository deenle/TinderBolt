# Telegram Bot with AI for practicing a date

---

## Bot will be capable of:
* Having a dialog;
* Practicing in dates;
* Generating cool descriptions for dating profiles on the sites;
* Giving tips for chatting on the dating site.

---

## Setup

Add environment variables to OS or to 'Run Configuration' in IntelliJ IDEA.
* **TG_BOT_NAME** - for bot name as `"full_name_of_chat_bot"`;
* **TG_BOT_TOKEN** - for bot token;
* **AI_TOKEN** - for AI token.

  #### Optionally: 
  * for access restriction:
    * **OWNER_ID** - Telegram user ID;
    * **BOT_ID** - Your Bot ID (in order to forward replies from AI).
  * for using proxy:
    * **PROXY_IP** - proxy IP address;
    * **PROXY_PORT** - proxy PORT.

## Running with Docker

Open IntelliJ IDEA and go to:

   * File>Project Structure>Artifacts> + (Add artifact)>JAR>From modules with dependencies>Select main class (Main.java )>OK>Apply. 

   * Then Build>Build Artifacts>Build. 

Then in the out/artifacts/ folder you will see your JAR.

Build and run your image:

```
docker build -t tgbot-image .

docker compose up -d
```

---
### Credits
* [JavaRush](https://javarush.com)

* [TelegramBots](https://github.com/rubenlagus/TelegramBots)


---

### License
MIT License