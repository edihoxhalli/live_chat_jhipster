import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ChatMessageMySuffixService } from './chat-message-my-suffix.service';

@Component({
    selector: 'jhi-chat-message-my-suffix-detail',
    templateUrl: './chat-message-my-suffix-detail.component.html'
})
export class ChatMessageMySuffixDetailComponent implements OnInit, OnDestroy {

    chatMessage: ChatMessageMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private chatMessageService: ChatMessageMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChatMessages();
    }

    load(id) {
        this.chatMessageService.find(id).subscribe((chatMessage) => {
            this.chatMessage = chatMessage;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChatMessages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chatMessageListModification',
            (response) => this.load(this.chatMessage.id)
        );
    }
}
