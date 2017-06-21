import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ChatMessageMySuffixPopupService } from './chat-message-my-suffix-popup.service';
import { ChatMessageMySuffixService } from './chat-message-my-suffix.service';

@Component({
    selector: 'jhi-chat-message-my-suffix-delete-dialog',
    templateUrl: './chat-message-my-suffix-delete-dialog.component.html'
})
export class ChatMessageMySuffixDeleteDialogComponent {

    chatMessage: ChatMessageMySuffix;

    constructor(
        private chatMessageService: ChatMessageMySuffixService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chatMessageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chatMessageListModification',
                content: 'Deleted an chatMessage'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('chatApp.chatMessage.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-chat-message-my-suffix-delete-popup',
    template: ''
})
export class ChatMessageMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatMessagePopupService: ChatMessageMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.chatMessagePopupService
                .open(ChatMessageMySuffixDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
