import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ChatTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChatMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/chat/chat-my-suffix-detail.component';
import { ChatMySuffixService } from '../../../../../../main/webapp/app/entities/chat/chat-my-suffix.service';
import { ChatMySuffix } from '../../../../../../main/webapp/app/entities/chat/chat-my-suffix.model';

describe('Component Tests', () => {

    describe('ChatMySuffix Management Detail Component', () => {
        let comp: ChatMySuffixDetailComponent;
        let fixture: ComponentFixture<ChatMySuffixDetailComponent>;
        let service: ChatMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ChatMySuffixDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChatMySuffixService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChatMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatMySuffixService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ChatMySuffix(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.chat).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
