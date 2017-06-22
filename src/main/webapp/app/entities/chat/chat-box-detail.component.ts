import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ChatMySuffix } from './chat-my-suffix.model';
import { ChatMySuffixService } from './chat-my-suffix.service';
import {AfterViewInit} from '@angular/core';    
import {ElementRef, ViewChild} from '@angular/core';



@Component({
    selector: 'chat-box-detail',
    templateUrl: './chat-box-detail.component.html',
    styleUrls: ['./chat-box-detail.component.css']
})
export class ChatBoxDetail implements OnInit, OnDestroy, AfterViewInit {

    @ViewChild('messageBox') message_box:ElementRef;
    
    @ViewChild('myname') myname:ElementRef

    chat: ChatMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private chatService: ChatMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngAfterViewInit() {
       //Copy in all the js code from the script.js. Typescript will complain but it works just fine
       //document.getElementById('message_box');
       console.log(this.myname);
        console.log(this.message_box);
        console.log(this.message_box.nativeElement);
        this.message_box.nativeElement.scrollTop =this.message_box.nativeElement.scrollHeight ;
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        
        

        this.registerChangeInChats();
    }

    load(id) {
        this.chatService.find(id).subscribe((chat) => {
            this.chat = chat;
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
