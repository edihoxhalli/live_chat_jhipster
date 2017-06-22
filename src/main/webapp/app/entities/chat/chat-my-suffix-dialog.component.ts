import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ChatMySuffix } from './chat-my-suffix.model';
import { ChatMySuffixPopupService } from './chat-my-suffix-popup.service';
import { ChatMySuffixService } from './chat-my-suffix.service';
import { User, UserService } from '../../shared';
import { Principal, ResponseWrapper } from '../../shared';
@Component({
    selector: 'jhi-chat-my-suffix-dialog',
    templateUrl: './chat-my-suffix-dialog.component.html'
})
export class ChatMySuffixDialogComponent implements OnInit {

    chat: ChatMySuffix;
    authorities: any[];
    isSaving: boolean;

    users: User[];
    currentAccount: any;
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private chatService: ChatMySuffixService,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { 
                this.principal.identity().then((account) => {
                    this.currentAccount = account;
                    this.users = res.json;
                    let index = 0;
                    for(index;index<this.users.length;index++){
                        if(this.currentAccount.login == this.users[index].login){
                            break;
                        }
                    }
                    this.users.splice(index, 1); 
                });
                
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.chat.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chatService.update(this.chat), false);
        } else {
            this.subscribeToSaveResponse(
                this.chatService.create(this.chat), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ChatMySuffix>, isCreated: boolean) {
        result.subscribe((res: ChatMySuffix) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ChatMySuffix, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'chatApp.chat.created'
            : 'chatApp.chat.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'chatListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-chat-my-suffix-popup',
    template: ''
})
export class ChatMySuffixPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatPopupService: ChatMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.chatPopupService
                    .open(ChatMySuffixDialogComponent, params['id']);
            } else {
                this.modalRef = this.chatPopupService
                    .open(ChatMySuffixDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
