import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ChatChatMySuffixModule } from './chat/chat-my-suffix.module';
// import { ChatChatMessageMySuffixModule } from './chat-message/chat-message-my-suffix.module';
import { ChatChatMessageMySuffixModule } from './chatmessage/chatmessage-my-suffix.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ChatChatMySuffixModule,
        // ChatChatMessageMySuffixModule,
        ChatChatMessageMySuffixModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatEntityModule {}
