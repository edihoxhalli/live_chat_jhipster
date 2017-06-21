import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ChatMessageMySuffixService } from './chat-message-my-suffix.service';

@Injectable()
export class ChatMessageMySuffixPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private chatMessageService: ChatMessageMySuffixService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.chatMessageService.find(id).subscribe((chatMessage) => {
                chatMessage.senttime = this.datePipe
                    .transform(chatMessage.senttime, 'yyyy-MM-ddThh:mm');
                this.chatMessageModalRef(component, chatMessage);
            });
        } else {
            return this.chatMessageModalRef(component, new ChatMessageMySuffix());
        }
    }

    chatMessageModalRef(component: Component, chatMessage: ChatMessageMySuffix): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chatMessage = chatMessage;
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
