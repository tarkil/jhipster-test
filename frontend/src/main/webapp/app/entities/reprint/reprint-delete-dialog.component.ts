import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Reprint } from './reprint.model';
import { ReprintPopupService } from './reprint-popup.service';
import { ReprintService } from './reprint.service';

@Component({
    selector: 'jhi-reprint-delete-dialog',
    templateUrl: './reprint-delete-dialog.component.html'
})
export class ReprintDeleteDialogComponent {

    reprint: Reprint;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private reprintService: ReprintService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['reprint']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.reprintService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reprintListModification',
                content: 'Deleted an reprint'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reprint-delete-popup',
    template: ''
})
export class ReprintDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private reprintPopupService: ReprintPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.reprintPopupService
                .open(ReprintDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
