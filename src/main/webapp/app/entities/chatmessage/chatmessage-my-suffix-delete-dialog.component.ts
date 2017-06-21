import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ChatmessageMySuffix } from './chatmessage-my-suffix.model';
import { ChatmessageMySuffixPopupService } from './chatmessage-my-suffix-popup.service';
import { ChatmessageMySuffixService } from './chatmessage-my-suffix.service';

@Component({
    selector: 'jhi-chatmessage-my-suffix-delete-dialog',
    templateUrl: './chatmessage-my-suffix-delete-dialog.component.html'
})
export class ChatmessageMySuffixDeleteDialogComponent {

    chatmessage: ChatmessageMySuffix;

    constructor(
        private chatmessageService: ChatmessageMySuffixService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chatmessageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chatmessageListModification',
                content: 'Deleted an chatmessage'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('chatApp.chatmessage.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-chatmessage-my-suffix-delete-popup',
    template: ''
})
export class ChatmessageMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatmessagePopupService: ChatmessageMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.chatmessagePopupService
                .open(ChatmessageMySuffixDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
