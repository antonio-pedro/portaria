import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOM, NewOM } from '../om.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOM for edit and NewOMFormGroupInput for create.
 */
type OMFormGroupInput = IOM | PartialWithRequiredKeyOf<NewOM>;

type OMFormDefaults = Pick<NewOM, 'id'>;

type OMFormGroupContent = {
  id: FormControl<IOM['id'] | NewOM['id']>;
  nome: FormControl<IOM['nome']>;
  sigla: FormControl<IOM['sigla']>;
  codom: FormControl<IOM['codom']>;
};

export type OMFormGroup = FormGroup<OMFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OMFormService {
  createOMFormGroup(oM: OMFormGroupInput = { id: null }): OMFormGroup {
    const oMRawValue = {
      ...this.getFormDefaults(),
      ...oM,
    };
    return new FormGroup<OMFormGroupContent>({
      id: new FormControl(
        { value: oMRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(oMRawValue.nome, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      sigla: new FormControl(oMRawValue.sigla, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      codom: new FormControl(oMRawValue.codom),
    });
  }

  getOM(form: OMFormGroup): IOM | NewOM {
    return form.getRawValue() as IOM | NewOM;
  }

  resetForm(form: OMFormGroup, oM: OMFormGroupInput): void {
    const oMRawValue = { ...this.getFormDefaults(), ...oM };
    form.reset(
      {
        ...oMRawValue,
        id: { value: oMRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OMFormDefaults {
    return {
      id: null,
    };
  }
}
