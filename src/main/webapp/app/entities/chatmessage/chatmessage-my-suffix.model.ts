import { BaseEntity } from './../../shared';

export class ChatmessageMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public story?: string,
        public senttime?: any,
        public chatId?: number,
        public userId?: number,
    ) {
    }
}
