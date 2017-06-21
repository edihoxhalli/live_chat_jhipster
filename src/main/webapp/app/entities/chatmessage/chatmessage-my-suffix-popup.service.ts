import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ChatmessageMySuffix } from './chatmessage-my-suffix.model';
import { ChatmessageMySuffixService } from './chatmessage-my-suffix.service';

@Injectable()
export class ChatmessageMySuffixPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private chatmessageService: ChatmessageMySuffixService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.chatmessageService.find(id).subscribe((chatmessage) => {
                chatmessage.senttime = this.datePipe
                    .transform(chatmessage.senttime, 'yyyy-MM-ddThh:mm');
                this.chatmessageModalRef(component, chatmessage);
            });
        } else {
            return this.chatmessageModalRef(component, new ChatmessageMySuffix());
        }
    }

    chatmessageModalRef(component: Component, chatmessage: ChatmessageMySuffix): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chatmessage = chatmessage;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
