package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = System.getenv("TG_BOT_NAME");
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TG_BOT_TOKEN");
    public static final String OPEN_AI_TOKEN = System.getenv("OPENAI_TOKEN");

    DialogMode currentMode = null;
    ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);


    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь
        String fileName;
        String message = getMessageText();

        // Allow to communicate only with the bot owner
        if (!System.getenv("OWNER_ID").isEmpty() &&
                update.getMessage().getFrom().getId() != Long.parseLong((System.getenv("OWNER_ID")))) return;

        if(message.startsWith("/start")) {
            currentMode = DialogMode.MAIN;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);
            sendTextMessage(loadMessage(fileName));
            return;
        }

        if(message.startsWith("/gpt")) {
            currentMode = DialogMode.GPT;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);
            sendTextMessage(loadMessage(fileName));
            return;
        }

        if(currentMode == DialogMode.GPT) {
            String prompt = loadPrompt(DialogMode.GPT.modeToLowerCase());
            String answer = chatGPT.sendMessage(prompt, message);
            sendTextMessage(answer);
            return;
        }


        // Bot menu
        showMainMenu(DialogMode.MAIN.getTitle(), "/start",
                DialogMode.PROFILE.getTitle(), "/profile",
                DialogMode.OPENER.getTitle(), "/opener",
                DialogMode.MESSAGE.getTitle(), "/message",
                DialogMode.DATE.getTitle(), "/date",
                DialogMode.GPT.getTitle(), "/gpt");

        // Buttons test
        sendTextButtonsMessage("Push the Button",
                "START", "start_btn",
                        "STOP", "stop_btn");
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
