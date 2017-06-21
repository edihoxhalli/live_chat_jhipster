import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ChatTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChatmessageMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix-detail.component';
import { ChatmessageMySuffixService } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix.service';
import { ChatmessageMySuffix } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix.model';

describe('Component Tests', () => {

    describe('ChatmessageMySuffix Management Detail Component', () => {
        let comp: ChatmessageMySuffixDetailComponent;
        let fixture: ComponentFixture<ChatmessageMySuffixDetailComponent>;
        let service: ChatmessageMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ChatmessageMySuffixDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChatmessageMySuffixService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChatmessageMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatmessageMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatmessageMySuffixService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ChatmessageMySuffix(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.chatmessage).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
