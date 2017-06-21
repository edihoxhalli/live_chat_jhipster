import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ChatMessageMySuffixPopupService } from './chat-message-my-suffix-popup.service';
import { ChatMessageMySuffixService } from './chat-message-my-suffix.service';
import { ChatMySuffix, ChatMySuffixService } from '../chat';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-chat-message-my-suffix-dialog',
    templateUrl: './chat-message-my-suffix-dialog.component.html'
})
export class ChatMessageMySuffixDialogComponent implements OnInit {

    chatMessage: ChatMessageMySuffix;
    authorities: any[];
    isSaving: boolean;

    chats: ChatMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private chatMessageService: ChatMessageMySuffixService,
        private chatService: ChatMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.chatService.query()
            .subscribe((res: ResponseWrapper) => { this.chats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.chatMessage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chatMessageService.update(this.chatMessage), false);
        } else {
            this.subscribeToSaveResponse(
                this.chatMessageService.create(this.chatMessage), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ChatMessageMySuffix>, isCreated: boolean) {
        result.subscribe((res: ChatMessageMySuffix) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ChatMessageMySuffix, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'chatApp.chatMessage.created'
            : 'chatApp.chatMessage.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'chatMessageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackChatById(index: number, item: ChatMySuffix) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-chat-message-my-suffix-popup',
    template: ''
})
export class ChatMessageMySuffixPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatMessagePopupService: ChatMessageMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.chatMessagePopupService
                    .open(ChatMessageMySuffixDialogComponent, params['id']);
            } else {
                this.modalRef = this.chatMessagePopupService
                    .open(ChatMessageMySuffixDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
