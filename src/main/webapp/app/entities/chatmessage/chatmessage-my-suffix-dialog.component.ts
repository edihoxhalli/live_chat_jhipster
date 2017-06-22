import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chatmessage-my-suffix.model';
import { ChatMessageMySuffixPopupService } from './chatmessage-my-suffix-popup.service';
import { ChatMessageMySuffixService } from './chatmessage-my-suffix.service';
import { ChatMySuffix, ChatMySuffixService } from '../chat';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-chatmessage-my-suffix-dialog',
    templateUrl: './chatmessage-my-suffix-dialog.component.html'
})
export class ChatMessageMySuffixDialogComponent implements OnInit {

    chatmessage: ChatMessageMySuffix;
    authorities: any[];
    isSaving: boolean;

    chats: ChatMySuffix[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private chatmessageService: ChatMessageMySuffixService,
        private chatService: ChatMySuffixService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.chatService.query()
            .subscribe((res: ResponseWrapper) => { this.chats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.chatmessage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chatmessageService.update(this.chatmessage), false);
        } else {
            this.subscribeToSaveResponse(
                this.chatmessageService.create(this.chatmessage), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ChatMessageMySuffix>, isCreated: boolean) {
        result.subscribe((res: ChatMessageMySuffix) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ChatMessageMySuffix, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'chatApp.chatmessage.created'
            : 'chatApp.chatmessage.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'chatmessageListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-chatmessage-my-suffix-popup',
    template: ''
})
export class ChatMessageMySuffixPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatmessagePopupService: ChatMessageMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.chatmessagePopupService
                    .open(ChatMessageMySuffixDialogComponent, params['id']);
            } else {
                this.modalRef = this.chatmessagePopupService
                    .open(ChatMessageMySuffixDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
