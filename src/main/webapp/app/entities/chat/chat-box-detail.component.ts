import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ChatMySuffix } from './chat-my-suffix.model';
import { ChatMySuffixService } from './chat-my-suffix.service';
import {AfterViewInit} from '@angular/core';    
import {QueryList, ElementRef, ViewChild} from '@angular/core';
import { ChatMessageMySuffixService } from '../chatmessage/chatmessage-my-suffix.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { ChatMessageMySuffix } from '../chatmessage/chatmessage-my-suffix.model';

@Component({
    selector: 'chat-box-detail',
    templateUrl: './chat-box-detail.component.html',
    styleUrls: ['./chat-box-detail.component.css']
})
export class ChatBoxDetail implements OnInit, OnDestroy, AfterViewInit {

    @ViewChild('messageBox', {read: ElementRef}) messageBox:ElementRef;

    chatmessages: ChatMessageMySuffix[];
    currentMessage:string = "";
    chat: ChatMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    
    constructor(
        private eventManager: JhiEventManager,
        private chatService: ChatMySuffixService,
        private route: ActivatedRoute,
        private chatmessageService: ChatMessageMySuffixService,
    ) {
    }

    ngAfterViewInit() {
        this.scrollDown();
    }

    scrollDown(){
        setTimeout(function() {
                let messagesDiv = document.getElementById('messageBox');
                messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }, 100);
    }

    sendMessage(){
        let chatmessage: ChatMessageMySuffix = new ChatMessageMySuffix();
        chatmessage.story = this.currentMessage;
        chatmessage.chatId = this.chat.id;
        this.chatmessageService.send(chatmessage).subscribe(
            (savedMessage) => {
                this.chatmessages.push(savedMessage);
                this.currentMessage="";
                this.scrollDown();
            }
        );
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
            
        });
        
        

        this.registerChangeInChats();
    }

    loadMessages() {
        this.chatmessageService.queryByChat(this.chat.id).subscribe(
            (res: ResponseWrapper) => {
                this.chatmessages = res.json;
                this.scrollDown();
            }
        );
    }

    load(id) {
        this.chatService.find(id).subscribe((chat) => {
            this.chat = chat;
            this.loadMessages();
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChats() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chatListModification',
            (response) => this.load(this.chat.id)
        );
    }
    
}
