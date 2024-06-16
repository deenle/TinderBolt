package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = System.getenv("TG_BOT_NAME");
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TG_BOT_TOKEN");
    public static final String OPEN_AI_TOKEN = System.getenv("OPENAI_TOKEN");

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь
        DialogMode currentMode;
        String fileName;
        String message = getMessageText();

        if(message.startsWith("/start")) {
            currentMode = DialogMode.MAIN;
            fileName = currentMode.name().toLowerCase();
            sendPhotoMessage(fileName);
            sendTextMessage(loadMessage(fileName));
            return;
        }

        // Bot menu
        showMainMenu(DialogMode.MAIN.name(), "/start",
                DialogMode.PROFILE.name(), "/profile",
                DialogMode.OPENER.name(), "/opener",
                DialogMode.MESSAGE.name(), "/message",
                DialogMode.DATE.name(), "/date",
                DialogMode.GPT.name(), "/gpt");

        sendTextButtonsMessage("Push the Button",
                "START", "start_btn",
                        "STOP", "stop_btn");
//        sendTextMessage("Hello from TinderBolt");
        if (getCallbackQueryButtonKey().equalsIgnoreCase("stop_btn")) {
            sendTextMessage("We are stopping");
        } else if (getCallbackQueryButtonKey().equalsIgnoreCase("start_btn")) {
            sendTextMessage("Let's continue");
        }


    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
