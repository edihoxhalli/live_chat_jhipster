import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ChatMessageMySuffixService } from './chat-message-my-suffix.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-chat-message-my-suffix',
    templateUrl: './chat-message-my-suffix.component.html'
})
export class ChatMessageMySuffixComponent implements OnInit, OnDestroy {
chatMessages: ChatMessageMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private chatMessageService: ChatMessageMySuffixService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.chatMessageService.query().subscribe(
            (res: ResponseWrapper) => {
                this.chatMessages = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInChatMessages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ChatMessageMySuffix) {
        return item.id;
    }
    registerChangeInChatMessages() {
        this.eventSubscriber = this.eventManager.subscribe('chatMessageListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
