import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chatmessage-my-suffix.model';
import { ChatMessageMySuffixService } from './chatmessage-my-suffix.service';

@Component({
    selector: 'jhi-chatmessage-my-suffix-detail',
    templateUrl: './chatmessage-my-suffix-detail.component.html'
})
export class ChatMessageMySuffixDetailComponent implements OnInit, OnDestroy {

    chatmessage: ChatMessageMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private chatmessageService: ChatMessageMySuffixService,
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
        this.chatmessageService.find(id).subscribe((chatmessage) => {
            this.chatmessage = chatmessage;
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
            'chatmessageListModification',
            (response) => this.load(this.chatmessage.id)
        );
    }
}
