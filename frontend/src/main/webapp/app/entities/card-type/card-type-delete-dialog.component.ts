import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { CardType } from './card-type.model';
import { CardTypePopupService } from './card-type-popup.service';
import { CardTypeService } from './card-type.service';

@Component({
    selector: 'jhi-card-type-delete-dialog',
    templateUrl: './card-type-delete-dialog.component.html'
})
export class CardTypeDeleteDialogComponent {

    cardType: CardType;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private cardTypeService: CardTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['cardType']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.cardTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cardTypeListModification',
                content: 'Deleted an cardType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-card-type-delete-popup',
    template: ''
})
export class CardTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private cardTypePopupService: CardTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.cardTypePopupService
                .open(CardTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
