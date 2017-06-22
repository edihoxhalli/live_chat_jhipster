import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chatmessage-my-suffix.model';
import { ChatMessageMySuffixService } from './chatmessage-my-suffix.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-chatmessage-my-suffix',
    templateUrl: './chatmessage-my-suffix.component.html'
})
export class ChatMessageMySuffixComponent implements OnInit, OnDestroy {
chatmessages: ChatMessageMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private chatmessageService: ChatMessageMySuffixService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.chatmessageService.query().subscribe(
            (res: ResponseWrapper) => {
                this.chatmessages = res.json;
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
        this.eventSubscriber = this.eventManager.subscribe('chatmessageListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
