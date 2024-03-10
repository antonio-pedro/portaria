import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOM } from '../om.model';
import { OMService } from '../service/om.service';
import { OMFormService, OMFormGroup } from './om-form.service';

@Component({
  standalone: true,
  selector: 'jhi-om-update',
  templateUrl: './om-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OMUpdateComponent implements OnInit {
  isSaving = false;
  oM: IOM | null = null;

  editForm: OMFormGroup = this.oMFormService.createOMFormGroup();

  constructor(
    protected oMService: OMService,
    protected oMFormService: OMFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oM }) => {
      this.oM = oM;
      if (oM) {
        this.updateForm(oM);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const oM = this.oMFormService.getOM(this.editForm);
    if (oM.id !== null) {
      this.subscribeToSaveResponse(this.oMService.update(oM));
    } else {
      this.subscribeToSaveResponse(this.oMService.create(oM));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOM>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(oM: IOM): void {
    this.oM = oM;
    this.oMFormService.resetForm(this.editForm, oM);
  }
}
