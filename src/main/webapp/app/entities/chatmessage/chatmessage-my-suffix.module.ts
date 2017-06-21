import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import { ChatAdminModule } from '../../admin/admin.module';
import {
    ChatmessageMySuffixService,
    ChatmessageMySuffixPopupService,
    ChatmessageMySuffixComponent,
    ChatmessageMySuffixDetailComponent,
    ChatmessageMySuffixDialogComponent,
    ChatmessageMySuffixPopupComponent,
    ChatmessageMySuffixDeletePopupComponent,
    ChatmessageMySuffixDeleteDialogComponent,
    chatmessageRoute,
    chatmessagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...chatmessageRoute,
    ...chatmessagePopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        ChatAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChatmessageMySuffixComponent,
        ChatmessageMySuffixDetailComponent,
        ChatmessageMySuffixDialogComponent,
        ChatmessageMySuffixDeleteDialogComponent,
        ChatmessageMySuffixPopupComponent,
        ChatmessageMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        ChatmessageMySuffixComponent,
        ChatmessageMySuffixDialogComponent,
        ChatmessageMySuffixPopupComponent,
        ChatmessageMySuffixDeleteDialogComponent,
        ChatmessageMySuffixDeletePopupComponent,
    ],
    providers: [
        ChatmessageMySuffixService,
        ChatmessageMySuffixPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatChatmessageMySuffixModule {}
